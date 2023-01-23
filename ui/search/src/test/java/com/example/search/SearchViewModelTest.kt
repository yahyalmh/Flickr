package com.example.search

import com.example.bookmark.BookmarksInteractorImpl
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.database.history.SearchHistoryEntity
import com.example.data.common.ext.RandomString
import com.example.data.common.model.Photo
import com.example.data.common.model.toEntity
import com.example.filckrsearch.search.FlickrSearchInteractorImpl
import com.example.history.SearchHistoryInteractorImpl
import com.example.search.SearchUiEvent.*
import com.example.search.SearchUiState.*
import com.example.search.SearchUiState.Retry
import com.example.search.nav.SearchRoute
import com.example.ui.common.test.MainDispatcherRule
import com.example.ui.common.test.thenEmitError
import com.example.ui.common.test.thenEmitNothing
import com.example.ui.common.utility.ImageDownloader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class, MainDispatcherRule::class)
internal class SearchViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var flickrSearchInteractor: FlickrSearchInteractorImpl

    @Mock
    private lateinit var bookmarksInteractor: BookmarksInteractorImpl

    @Mock
    private lateinit var searchHistoryInteractor: SearchHistoryInteractorImpl

    @Mock
    private lateinit var searchRoute: SearchRoute

    @Mock
    private lateinit var imageDownloader: ImageDownloader

    private lateinit var searchViewModel: SearchViewModel


    private lateinit var photosStub: List<Photo>
    private lateinit var bookmarkedPhotos: MutableList<PhotoEntity>
    private lateinit var searchHistories: List<SearchHistoryEntity>
    private val sampleQuery = "query"

    @BeforeEach
    fun setup() {
        photosStub = photosStub()
        bookmarkedPhotos = photoEntitiesStub().toMutableList()
        searchHistories = searchHistoriesEntityStub()
    }

    @Test
    fun `Given start searching WHEN there is search history THEN first state is histories`() =
        runTest {
            whenever(searchHistoryInteractor.getHistories()).thenReturn(flowOf(searchHistories))

            searchViewModel = SearchViewModel(
                flickrSearchInteractor = flickrSearchInteractor,
                bookmarksInteractor = bookmarksInteractor,
                searchRoute = searchRoute,
                imageDownloader = imageDownloader,
                searchHistoryInteractor = searchHistoryInteractor
            )
            Assertions.assertTrue(searchViewModel.state.value is HistoryLoaded)
        }

    @Test
    fun `Given start searching WHEN there is no search history THEN first state is start page`() =
        runTest {
            whenever(searchHistoryInteractor.getHistories()).thenReturn(flowOf(emptyList()))

            searchViewModel = SearchViewModel(
                flickrSearchInteractor = flickrSearchInteractor,
                bookmarksInteractor = bookmarksInteractor,
                searchRoute = searchRoute,
                imageDownloader = imageDownloader,
                searchHistoryInteractor = searchHistoryInteractor
            )
            Assertions.assertTrue(searchViewModel.state.value is Start)
        }

    @Test
    fun `WHEN searching return data THEN state is Loaded`() = runTest {
        whenever(flickrSearchInteractor.search(any(), any(), any())).thenReturn(flowOf(photosStub))
        whenever(searchHistoryInteractor.getHistories()).thenReturn(flowOf(searchHistories))
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))

        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor
        )
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceUntilIdle()

        val uiState = searchViewModel.state.value
        Assertions.assertEquals(photosStub, uiState.result)
        Assertions.assertEquals(bookmarkedPhotos, uiState.bookmarkedPhotos)
        Assertions.assertFalse(uiState.isLoading)
    }

    @Test
    fun `WHEN searching returns error THEN state is AutoRetry`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()
        whenever(flickrSearchInteractor.search(any(), any(), any()))
            .thenReturn(flow { throw IOException() })

        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor
        )
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceTimeBy(1000)
        Assertions.assertTrue(searchViewModel.state.value is AutoRetry)
    }

    @Test
    fun `WHEN searching returns error THEN state is AutoRetry THEN state is Loaded`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()
        val time = currentTime // while running all test the time will be accumulated
        whenever(flickrSearchInteractor.search(any(), any(), any()))
            .thenReturn(flow {
                if (currentTime < time + 3000) {
                    throw IOException()
                } else {
                    emit(photosStub)
                }
            })

        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor
        )
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceTimeBy(1000)
        Assertions.assertTrue(searchViewModel.state.value is AutoRetry)

        advanceTimeBy(4000)
        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state.value is DataLoaded)
    }


    @Test
    fun `WHEN  searching returns error THEN after a while state is Retry`() = runTest {
        whenever(flickrSearchInteractor.search(any(), any(), any())).thenEmitError(IOException())
        whenever(bookmarksInteractor.getBookmarks()).thenEmitNothing()
        whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()

        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor
        )
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceTimeBy(1000)
        Assertions.assertTrue(searchViewModel.state.value is AutoRetry)

        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state.value is Retry)
    }

    @Test
    fun `GIVEN retry event THEN data load successfully`() = runTest {
        whenever(flickrSearchInteractor.search(any(), any(), any())).thenEmitError(IOException())

        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor
        )
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state.value is Retry)

        whenever(flickrSearchInteractor.search(any(), any(), any())).thenReturn(
            flowOf(photosStub)
        )
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        searchViewModel.onEvent(SearchUiEvent.Retry)

        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state.value is DataLoaded)
    }

    @Test
    fun `GIVEN bookmark event THEN item added or removed from bookmarks`() = runTest {
        whenever(flickrSearchInteractor.search(any(), any(), any())).thenReturn(flowOf(photosStub))
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()
        val randomFileAddress = RandomString.next()
        whenever(imageDownloader.downloadToFiles(any(), any())).thenReturn(randomFileAddress)

        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor
        )
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state.value is DataLoaded)

        searchViewModel.onEvent(OnBookmark(photosStub.last()))
        advanceUntilIdle()

        verify(bookmarksInteractor).addBookmark(any())

        val expected = photosStub.last().toEntity(randomFileAddress)
        bookmarkedPhotos.add(expected)

        searchViewModel.onEvent(OnBookmark(photosStub.last()))
        advanceUntilIdle()

        verify(bookmarksInteractor).removeBookmark(bookmarkedPhotos.last())
    }
}