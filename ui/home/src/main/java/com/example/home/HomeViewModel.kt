package com.example.home

import androidx.lifecycle.viewModelScope
import com.example.bookmark.BookmarksInteractor
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.home.HomeUiState.*
import com.example.home.nav.HomeRoute
import com.example.ui.common.BaseViewModel
import com.example.ui.common.UIEvent
import com.example.ui.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val bookmarksInteractor: BookmarksInteractor,
    private val homeRoute: HomeRoute,
) : BaseViewModel<HomeUiState, HomeUiEvent>(Loading) {

    init {
        fetchBookmarked()
    }

    private fun fetchBookmarked() {
        bookmarksInteractor.getBookmarks()
            .onStart { setState(Loading) }
            .onEach { photos ->
                when {
                    photos.isEmpty() -> setState(Empty(state))
                    else -> setState(Loaded(state.copy(bookmarkedPhotos = photos)))
                }
            }
            .catch { e -> handleRetry(e) }
            .launchIn(viewModelScope)
    }

    private fun handleRetry(e: Throwable) = setState(Retry(state.copy(retryMessage = e.message)))

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnRetry -> fetchBookmarked()
            is HomeUiEvent.OnBookmarkClick -> handleBookmark(event.photoId)
            HomeUiEvent.OnSearchClick -> homeRoute.navigateToSearch()
            is HomeUiEvent.OnPhotoClick -> homeRoute.navigateToDetail(event.photoId)
        }
    }

    private fun handleBookmark(photoId: String) {
        viewModelScope.launch {
            val bookmarkedPhotos = bookmarksInteractor.getBookmarks().firstOrNull()
            bookmarkedPhotos?.find { it.id == photoId }
                ?.let {
                    File(it.localAddress).delete()
                    bookmarksInteractor.removeBookmark(it)
                }
        }
    }
}

open class HomeUiState(
    val bookmarkedPhotos: List<PhotoEntity> = emptyList(),
    val retryMessage: String? = null,
) : UIState {
    val isLoaded: Boolean
        get() = this is Loaded
    val isLoading: Boolean
        get() = this is Loading
    val isRetry: Boolean
        get() = this is Retry
    val isEmpty: Boolean
        get() = this is Empty

    constructor(state: HomeUiState) : this(
        state.bookmarkedPhotos,
        state.retryMessage,
    )

    fun copy(
        bookmarkedPhotos: List<PhotoEntity> = this.bookmarkedPhotos,
        retryMessage: String? = this.retryMessage,
    ) = HomeUiState(
        bookmarkedPhotos,
        retryMessage,
    )

    object Loading : HomeUiState()
    class Retry(state: HomeUiState) : HomeUiState(state)
    class Empty(state: HomeUiState) : HomeUiState(state)
    class Loaded(state: HomeUiState) : HomeUiState(state)
}

sealed interface HomeUiEvent : UIEvent {
    object OnRetry : HomeUiEvent
    object OnSearchClick : HomeUiEvent
    class OnBookmarkClick(val photoId: String) : HomeUiEvent
    class OnPhotoClick(val photoId: String) : HomeUiEvent
}