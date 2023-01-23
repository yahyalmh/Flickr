package com.example.home

import com.example.bookmark.BookmarksInteractor
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.home.nav.HomeRoute
import com.example.ui.common.test.MainDispatcherRule
import com.example.ui.common.test.thenEmitError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class, MainDispatcherRule::class)
internal class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var bookmarksInteractor: BookmarksInteractor

    @Mock
    lateinit var homeRoute: HomeRoute
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var photoEntities: List<PhotoEntity>

    @BeforeEach
    fun setup() {
        photoEntities = photoEntitiesStub()
    }

    @Test
    fun `WHEN fetching bookmarks return data THEN ui state is Loaded`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(
            flowOf(photoEntities)
        )

        homeViewModel = HomeViewModel(bookmarksInteractor, homeRoute)
        val uiState = homeViewModel.state.value

        Assertions.assertTrue(uiState is HomeUiState.Loaded)
        Assertions.assertEquals(photoEntities, uiState.bookmarkedPhotos)
        Assertions.assertFalse(uiState.isLoading)
    }

    @Test
    fun `WHEN fetching bookmarks has delay THEN ui state is Loading`() = runTest {
        whenever(bookmarksInteractor.getBookmarks())
            .thenReturn(flow {
                delay(3000)
                emit(photoEntities)
            })

        homeViewModel = HomeViewModel(bookmarksInteractor, homeRoute)
        Assertions.assertTrue(homeViewModel.state.value is HomeUiState.Loading)

        advanceUntilIdle()
        Assertions.assertTrue(homeViewModel.state.value is HomeUiState.Loaded)
    }

    @Test
    fun `WHEN fetching bookmarks return error THEN ui state is Retry`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenEmitError(IOException())

        homeViewModel = HomeViewModel(bookmarksInteractor, homeRoute)

        advanceUntilIdle()
        Assertions.assertTrue(homeViewModel.state.value is HomeUiState.Retry)
    }


    @Test
    fun `GIVEN retry event THEN data load successfully`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenEmitError(IOException())

        homeViewModel = HomeViewModel(bookmarksInteractor, homeRoute)
        advanceUntilIdle()
        Assertions.assertTrue(homeViewModel.state.value is HomeUiState.Retry)

        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(photoEntities))
        homeViewModel.onEvent(HomeUiEvent.OnRetry)

        advanceUntilIdle()
        Assertions.assertTrue(homeViewModel.state.value is HomeUiState.Loaded)
    }

    @Test
    fun `GIVEN bookmark event THEN item removed from bookmarks`() = runTest {
        whenever(bookmarksInteractor.getBookmarks()).thenReturn(flowOf(photoEntities))

        homeViewModel = HomeViewModel(bookmarksInteractor, homeRoute)
        advanceUntilIdle()
        Assertions.assertTrue(homeViewModel.state.value is HomeUiState.Loaded)

        homeViewModel.onEvent(HomeUiEvent.OnBookmarkClick(photoEntities.last().id))
        advanceUntilIdle()

        verify(bookmarksInteractor).removeBookmark(photoEntities.last())
    }
}