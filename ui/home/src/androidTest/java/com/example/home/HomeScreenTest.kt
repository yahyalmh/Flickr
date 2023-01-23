package com.example.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ui.common.test.TestTag
import com.example.ui.common.test.getString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.flickr.ui.common.R.string as commonString

@RunWith(AndroidJUnit4::class)
internal class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun test_homeScreen_loading_state() {
        with(composeTestRule) {
            setContent { HomeScreenContent(uiState = HomeUiState.Loading) }
            onNode(hasTestTag(TestTag.SHIMMER_VIEW)).assertIsDisplayed()
        }
    }

    @Test
    fun test_homeScreen_retry_state() {
        with(composeTestRule) {
            setContent { HomeScreenContent(uiState = HomeUiState.Retry()) }
            onNodeWithText(getString(commonString.retry)).assertIsDisplayed()
            onNodeWithContentDescription(getString(commonString.warningIconDescription)).assertIsDisplayed()
            onNodeWithText(getString(commonString.defaultErrorHint)).assertIsDisplayed()
        }
    }

    @Test
    fun test_homeScreen_data_state() {
        with(composeTestRule) {
            val photoEntities = photoEntitiesStub()
            setContent { HomeScreenContent(uiState = HomeUiState.Loaded(photoEntities)) }
            onNode(hasScrollToIndexAction()).assertIsDisplayed()
            onNode(hasScrollAction()).performScrollToIndex(photoEntities.size - 1)
            onAllNodesWithContentDescription(getString(commonString.favoriteIconDescription))
                .assertCountEquals(photoEntities.size)
        }
    }
}
