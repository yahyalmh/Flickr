package com.example.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.example.bookmark.BookmarksInteractor
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.database.history.SearchHistoryEntity
import com.example.data.common.model.Photo
import com.example.data.common.model.toEntity
import com.example.filckrsearch.search.FlickrSearchInteractor
import com.example.history.SearchHistoryInteractor
import com.example.search.SearchUiState.AutoRetry
import com.example.search.SearchUiState.DataLoaded
import com.example.search.SearchUiState.Empty
import com.example.search.SearchUiState.HistoryLoaded
import com.example.search.SearchUiState.Loading
import com.example.search.SearchUiState.Pagination
import com.example.search.SearchUiState.Retry
import com.example.search.SearchUiState.Start
import com.example.search.nav.SearchRoute
import com.example.ui.common.BaseViewModel
import com.example.ui.common.UIEvent
import com.example.ui.common.UIState
import com.example.ui.common.connectivity.ConnectivityMonitor
import com.example.ui.common.ext.retryOnNetworkConnection
import com.example.ui.common.utility.ImageDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val flickrSearchInteractor: FlickrSearchInteractor,
    private val bookmarksInteractor: BookmarksInteractor,
    private val searchRoute: SearchRoute,
    private val imageDownloader: ImageDownloader,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val connectivityMonitor: ConnectivityMonitor,
) : BaseViewModel<SearchUiState, SearchUiEvent>(Start) {
    @VisibleForTesting
    val searchFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private lateinit var fetchDataJob: Job
    private var paginationStartPageNumber: Int = 2
    private var defaultPageSize: Int = 25

    init {
        fetchSearchHistory()
        observeSearchQueryChange()
    }

    private fun fetchSearchHistory() {
        combine(
            searchFlow.onStart { emit("") }, searchHistoryInteractor.getHistories()
        ) { query, history ->
            if (query.isBlank() && query.isEmpty()) {
                cancelFetchDataJob()
                if (history.isEmpty().not()) {
                    setState(HistoryLoaded(history))
                } else {
                    setState(Start)
                }
            }
        }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeSearchQueryChange() {
        val searchDelayInterval: Long = 300
        searchFlow.debounce(searchDelayInterval)
            .filter { it.isNotEmpty() && it.isNotBlank() }
            .distinctUntilChanged { old, new -> isValuesDistinct(old, new) }
            .mapLatest { query ->
                cancelFetchDataJob()
                searchQuery(query)
            }.launchIn(viewModelScope)
    }

    private fun isValuesDistinct(old: String, new: String): Boolean {
        // Search the same value in the retry and history view
        val shouldSearchSameValue = state.isRetry || state.isShowingHistory
        return if (old == new && shouldSearchSameValue) {
            false
        } else {
            old == new
        }
    }

    private fun cancelFetchDataJob() {
        if (::fetchDataJob.isInitialized && fetchDataJob.isActive) {
            fetchDataJob.cancel()
        }
    }

    private fun searchQuery(query: String) {
        fetchDataJob = combine(
            flickrSearchInteractor.search(query = query, page = 1, perPage = defaultPageSize),
            bookmarksInteractor.getBookmarks()
        ) { result, bookmarks ->
            setLoadedStat(result, query, bookmarks)
        }.onStart { setState(Loading(query = query, state.result)) }
            .retryOnNetworkConnection(connectivityMonitor) { e -> handleAutoRetry(e) }
            .catch { e -> handleError(e) }
            .launchIn(viewModelScope)
    }

    private fun setLoadedStat(
        result: List<Photo>, query: String, bookmarks: List<PhotoEntity>
    ) = when {
        result.isEmpty() -> setState(Empty(query))
        else -> {
            setState(
                DataLoaded(
                    result = result,
                    bookmarkedPhotos = bookmarks,
                    query = query,
                )
            )
        }
    }

    private fun handleError(e: Throwable) {
        setState(Retry(e.message, query = state.query))
    }

    private fun handleAutoRetry(e: Throwable) {
        setState(AutoRetry(e.message, query = state.query))
    }

    override fun onEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.Retry -> searchFlow.tryEmit(state.query)
            is SearchUiEvent.QueryChange -> searchFlow.tryEmit(event.text)
            is SearchUiEvent.OnBookmark -> handleBookmark(event.photo)
            SearchUiEvent.ClosSearch -> searchRoute.popBackStack()
            is SearchUiEvent.OnHistoryClick -> searchFlow.tryEmit(event.history)
            is SearchUiEvent.OnClearHistoryClick -> viewModelScope.launch {
                searchHistoryInteractor.removeHistory(event.history)
            }
            is SearchUiEvent.OnSaveSearch -> viewModelScope.launch {
                searchHistoryInteractor.addHistory(event.text)
            }
            SearchUiEvent.OnPagination -> fetchNextPage()
            is SearchUiEvent.OnPhotoClick -> searchRoute.navigateToDetail(event.photoId)
        }
    }

    private fun handleBookmark(photo: Photo) = viewModelScope.launch {
        bookmarksInteractor
            .getBookmarks()
            .firstOrNull()
            ?.find { it.id == photo.id }
            .let { photoEntity ->
                if (photoEntity == null) {
                    addBookmark(photo)
                } else {
                    removeBookmark(photoEntity)
                }
            }
    }

    private suspend fun removeBookmark(photoEntity: PhotoEntity) {
        val isImageFileDeleted = File(photoEntity.localAddress).delete()
        if (isImageFileDeleted) {
            bookmarksInteractor.removeBookmark(photoEntity)
        }
    }

    private suspend fun addBookmark(photo: Photo) {
        imageDownloader.downloadToFiles(
            imageUrl = photo.getImageUrl(),
            fileName = photo.id
        )?.let { imageAddress ->
            bookmarksInteractor.addBookmark(photo.toEntity(localAddress = imageAddress))
        }
    }


    private fun fetchNextPage() {
        if (state.isLoading || state.isPagination) {
            return
        }

        cancelFetchDataJob()
        fetchDataJob = combine(
            flickrSearchInteractor.search(
                query = state.query,
                page = paginationStartPageNumber,
                perPage = defaultPageSize
            ), bookmarksInteractor.getBookmarks()
        ) { result, bookmarks ->
            paginationStartPageNumber += 1
            setLoadedStat(state.result + result, state.query, bookmarks)
        }
            .onStart { setState(Pagination(state.query, state.result)) }
            .retryOnNetworkConnection(connectivityMonitor)
            .catch { e -> handleError(e) }
            .launchIn(viewModelScope)
    }
}

sealed class SearchUiState(
    val result: List<Photo> = emptyList(),
    val bookmarkedPhotos: List<PhotoEntity> = emptyList(),
    val histories: List<SearchHistoryEntity> = emptyList(),
    val isKeyboardHidden: Boolean = false,
    val retryMsg: String? = null,
    val autoRetryMsg: String? = null,
    var query: String,
) : UIState {
    val isLoaded: Boolean
        get() = this is DataLoaded
    val isLoading: Boolean
        get() = this is Loading
    val isRetry: Boolean
        get() = this is Retry
    val isAutoRetry: Boolean
        get() = this is AutoRetry
    val isEmpty: Boolean
        get() = this is Empty
    val isStart: Boolean
        get() = this is Start
    val isShowingHistory: Boolean
        get() = this is HistoryLoaded
    val isPagination: Boolean
        get() = this is Pagination

    class Loading(query: String, result: List<Photo>) :
        SearchUiState(query = query, result = result)

    class Empty(query: String = "") : SearchUiState(query = query)
    object Start : SearchUiState(query = "")
    class Pagination(query: String, result: List<Photo>) :
        SearchUiState(query = query, result = result)

    class Retry(retryMsg: String? = null, query: String) : SearchUiState(
        retryMsg = retryMsg, isKeyboardHidden = true, query = query
    )

    class AutoRetry(autoRetryMsg: String? = null, query: String) : SearchUiState(
        isKeyboardHidden = true, autoRetryMsg = autoRetryMsg, query = query
    )

    class DataLoaded(
        result: List<Photo>,
        bookmarkedPhotos: List<PhotoEntity>,
        query: String,
    ) : SearchUiState(
        result = result,
        bookmarkedPhotos = bookmarkedPhotos,
        query = query,
    )

    class HistoryLoaded(histories: List<SearchHistoryEntity>) : SearchUiState(
        histories = histories, query = ""
    )
}

sealed interface SearchUiEvent : UIEvent {
    object Retry : SearchUiEvent
    class QueryChange(val text: String) : SearchUiEvent
    object ClosSearch : SearchUiEvent
    object OnPagination : SearchUiEvent
    class OnSaveSearch(val text: String) : SearchUiEvent
    class OnBookmark(val photo: Photo) : SearchUiEvent
    class OnPhotoClick(val photoId: String) : SearchUiEvent
    class OnHistoryClick(val history: String) : SearchUiEvent
    class OnClearHistoryClick(val history: String) : SearchUiEvent
}