package com.example.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bookmark.BookmarksInteractor
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.model.PhotoDetail
import com.example.data.common.model.toEntity
import com.example.detail.DetailUiState.Loaded
import com.example.detail.DetailUiState.Loading
import com.example.detail.DetailUiState.Retry
import com.example.detail.DetailUiState.Start
import com.example.detail.nav.DetailRoute
import com.example.filckrsearch.detail.FlickrDetailInteractor
import com.example.ui.common.BaseViewModel
import com.example.ui.common.UIEvent
import com.example.ui.common.UIState
import com.example.ui.common.utility.ImageDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val detailRoute: DetailRoute,
    private val imageDownloader: ImageDownloader,
    private val bookmarksInteractor: BookmarksInteractor,
    private val flickrDetailInteractor: FlickrDetailInteractor,
) : BaseViewModel<DetailUiState, DetailUiEvent>(Start) {
    private val detailArgs: DetailRoute.DetailArgs = DetailRoute.DetailArgs(savedStateHandle)

    init {
        fetchPhotoDetail()
    }

    private fun fetchPhotoDetail() {
        combine(
            flickrDetailInteractor.getPhotoDetail(detailArgs.photoId),
            bookmarksInteractor.getBookmarks()
        ) { photoDetail, bookmarks ->
            setState(
                Loaded(
                    state.copy(
                        photoDetail = photoDetail,
                        isBookmarked = bookmarks.any { it.id == photoDetail.id })
                )
            )
        }
            .onStart { setState(Loading) }
            .catch { e -> handleError(e) }
            .launchIn(viewModelScope)
    }

    private fun handleError(e: Throwable) = setState(Retry(state.copy(retryMessage = e.message)))

    override fun onEvent(event: DetailUiEvent) {
        when (event) {
            DetailUiEvent.Retry -> fetchPhotoDetail()
            DetailUiEvent.NavigationBack -> detailRoute.popBackStack()
            is DetailUiEvent.OnBookmarkClick -> handleBookmark(event.photoDetail)

        }
    }

    private fun handleBookmark(photoDetail: PhotoDetail?) = viewModelScope.launch {
        if (photoDetail == null) return@launch
        bookmarksInteractor.getBookmarks()
            .firstOrNull()
            ?.find { it.id == photoDetail.id }
            .let { photoEntity ->
                if (photoEntity == null) {
                    addBookmark(photoDetail)
                } else {
                    removeBookmark(photoEntity)
                }
            }
    }

    private suspend fun addBookmark(photoDetail: PhotoDetail) {
        imageDownloader.downloadToFiles(
            imageUrl = photoDetail.getImageUrl(),
            fileName = photoDetail.id
        )
            ?.let { imageAddress ->
                bookmarksInteractor.addBookmark(photoDetail.toEntity(localAddress = imageAddress))
            }
    }

    private suspend fun removeBookmark(it: PhotoEntity) {
        val isImageFileDeleted = File(it.localAddress).delete()
        if (isImageFileDeleted) {
            bookmarksInteractor.removeBookmark(it)
        }
    }
}

open class DetailUiState(
    val photoDetail: PhotoDetail? = null,
    val isBookmarked: Boolean = false,
    val retryMessage: String? = null,
) : UIState {
    val isLoaded: Boolean
        get() = this is Loaded
    val isLoading: Boolean
        get() = this is Loading
    val isRetry: Boolean
        get() = this is Retry


    constructor(state: DetailUiState) : this(
        state.photoDetail,
        state.isBookmarked,
        state.retryMessage,
    )

    fun copy(
        photoDetail: PhotoDetail? = this.photoDetail,
        isBookmarked: Boolean = this.isBookmarked,
        retryMessage: String? = this.retryMessage,
    ) = DetailUiState(
        photoDetail,
        isBookmarked,
        retryMessage,
    )

    object Start : DetailUiState()
    object Loading : DetailUiState()
    class Retry(state: DetailUiState) : DetailUiState(state)
    class Loaded(state: DetailUiState) : DetailUiState(state)
}

sealed interface DetailUiEvent : UIEvent {
    object Retry : DetailUiEvent
    object NavigationBack : DetailUiEvent
    class OnBookmarkClick(val photoDetail: PhotoDetail?) : DetailUiEvent
}
