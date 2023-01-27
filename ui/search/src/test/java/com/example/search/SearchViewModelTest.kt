package com.example.search

import com.example.bookmark.BookmarksInteractorImpl
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.database.history.SearchHistoryEntity
import com.example.data.common.model.Photo
import com.example.data.common.model.toEntity
import com.example.filckrsearch.search.FlickrSearchInteractorImpl
import com.example.history.SearchHistoryInteractorImpl
import com.example.search.SearchUiEvent.*
import com.example.search.SearchUiState.*
import com.example.search.SearchUiState.Retry
import com.example.search.nav.SearchRoute
import com.example.ui.common.connectivity.ConnectivityMonitor
import com.example.ui.common.test.MainDispatcherRule
import com.example.ui.common.test.thenEmitError
import com.example.ui.common.test.thenEmitNothing
import com.example.ui.common.utility.ImageDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


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

    @Mock
    private lateinit var connectivityMonitor: ConnectivityMonitor

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

    private fun initializeViewmodel() {
        searchViewModel = SearchViewModel(
            flickrSearchInteractor = flickrSearchInteractor,
            bookmarksInteractor = bookmarksInteractor,
            searchRoute = searchRoute,
            imageDownloader = imageDownloader,
            searchHistoryInteractor = searchHistoryInteractor,
            connectivityMonitor = connectivityMonitor,
        )
    }

    @Test
    fun `Given start searching WHEN there is search history THEN first state is histories`() =
        runTest {
            whenever(searchHistoryInteractor.getHistories()).thenReturn(flowOf(searchHistories))

            initializeViewmodel()
            Assertions.assertTrue(searchViewModel.state is HistoryLoaded)
        }

    @Test
    fun `Given start searching WHEN there is no search history THEN first state is start page`() =
        runTest {
            whenever(searchHistoryInteractor.getHistories()).thenReturn(flowOf(emptyList()))

            initializeViewmodel()
            Assertions.assertTrue(searchViewModel.state is Start)
        }

    @Test
    fun `WHEN searching return data THEN state is Loaded`() = runTest {
        whenever(flickrSearchInteractor.search(any(), any(), any())).thenReturn(flowOf(photosStub))
        whenever(searchHistoryInteractor.getHistories()).thenReturn(flowOf(searchHistories))
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))

        initializeViewmodel()
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceUntilIdle()

        val uiState = searchViewModel.state
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
        whenever(connectivityMonitor.isOnline).thenReturn(flow {
            emit(false)
            delay(1000)
            emit(true)
        })

        initializeViewmodel()
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceTimeBy(1000)
        Assertions.assertTrue(searchViewModel.state is AutoRetry)
    }

    @Test
    fun `WHEN searching returns error THEN state is AutoRetry THEN state is Loaded`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()
        val waitTime = currentTime + 1000
        whenever(connectivityMonitor.isOnline).thenReturn(flow {
            emit(false)
            delay(1000)
            emit(true)
        })
        whenever(flickrSearchInteractor.search(any(), any(), any()))
            .thenReturn(flow {
                if (currentTime < waitTime) {
                    throw IOException()
                } else {
                    emit(photosStub)
                }
            })

        initializeViewmodel()
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceTimeBy(1000)
        Assertions.assertTrue(searchViewModel.state is AutoRetry)

        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state is Loaded)
    }

    @Test
    fun `WHEN searching returns error THEN state is AutoRetry THEN state is Retry`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()
        whenever(connectivityMonitor.isOnline).thenReturn(flow {
            emit(false)
            delay(1000)
            emit(true)
        })
        whenever(flickrSearchInteractor.search(any(), any(), any()))
            .thenReturn(flow {
                throw IOException()
            })

        initializeViewmodel()
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceTimeBy(1000)
        Assertions.assertTrue(searchViewModel.state is AutoRetry)

        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state is Retry)
    }


    @Test
    fun `GIVEN retry event THEN data load successfully`() = runTest {
        whenever(flickrSearchInteractor.search(any(), any(), any())).thenEmitError(IOException())

        initializeViewmodel()
        searchViewModel.searchFlow.emit(sampleQuery)
        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state is Retry)

        whenever(flickrSearchInteractor.search(any(), any(), any())).thenReturn(
            flowOf(photosStub)
        )
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
        searchViewModel.onEvent(SearchUiEvent.Retry)

        advanceUntilIdle()
        Assertions.assertTrue(searchViewModel.state is Loaded)
    }

    @Test
    fun `GIVEN bookmark event THEN item added or removed from bookmarks`(@TempDir tempDir: Path) =
        runTest {
            whenever(flickrSearchInteractor.search(any(), any(), any())).thenReturn(
                flowOf(
                    photosStub
                )
            )
            whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(bookmarkedPhotos))
            whenever(searchHistoryInteractor.getHistories()).thenEmitNothing()

            val tmpImageFileAddress = withContext(Dispatchers.IO) {
                Files.createFile(tempDir.resolve("image.jpeg"))
            }.toString()

            whenever(imageDownloader.downloadToFiles(any(), any())).thenReturn(tmpImageFileAddress)
            initializeViewmodel()
            searchViewModel.searchFlow.emit(sampleQuery)
            advanceUntilIdle()
            Assertions.assertTrue(searchViewModel.state is Loaded)

            searchViewModel.onEvent(OnBookmark(photosStub.last()))
            advanceUntilIdle()

            verify(bookmarksInteractor).addBookmark(any())

            val expected = photosStub.last().toEntity(tmpImageFileAddress)
            bookmarkedPhotos.add(expected)

            searchViewModel.onEvent(OnBookmark(photosStub.last()))
            advanceUntilIdle()

            verify(bookmarksInteractor).removeBookmark(bookmarkedPhotos.last())
        }
}