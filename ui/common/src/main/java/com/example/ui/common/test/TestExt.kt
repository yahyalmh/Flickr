package com.example.ui.common.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.rules.TestRule
import org.mockito.stubbing.OngoingStubbing

fun <T : Any> OngoingStubbing<Flow<T>>.thenEmitError(e: Throwable) {
    thenReturn(flow { throw e })
}

fun <T : Any> OngoingStubbing<Flow<T>>.thenEmitNothing() {
    thenReturn(flow {})
}

fun <R : TestRule, T : ComponentActivity> AndroidComposeTestRule<R, T>.wait(timeoutMillis: Long) =
    waitUntil(timeoutMillis) {
        mainClock.currentTime >= timeoutMillis
    }

fun <R : TestRule, T : ComponentActivity> AndroidComposeTestRule<R, T>.logTree() =
    onRoot(useUnmergedTree = true).printToLog("LogTree")

fun <R : TestRule, T : ComponentActivity> AndroidComposeTestRule<R, T>.getString(id: Int) =
    activity.getString(id)

fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.waitUntilDisplayed(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5000,
) {
    waitUntil(timeoutMillis) {
        try {
            onNode(matcher).assertIsDisplayed()
            true
        } catch (e: AssertionError) {
            false
        }
    }
}

fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.scrollToEnd(
    matcher: SemanticsMatcher,
    step: Int = 5
) {
    try {
        for (index in 0..Int.MAX_VALUE step step) {
            onNode(matcher).performScrollToIndex(index)
        }
    } catch (e: IllegalArgumentException) {
        e.message?.let { message ->
            val numbers = "\\d+".toRegex().findAll(message).map { it.groupValues[0].toInt() }
            val lastIndex = numbers.filter { it > 0 }.min() - 1
            if (lastIndex >= 0) {
                onNode(hasScrollToIndexAction()).performScrollToIndex(lastIndex)
            }
        }
    }
}