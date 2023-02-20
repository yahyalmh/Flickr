package com.example.guide

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberSegmentBarState(
    currentSegmentIndex: Int = 0,
    segmentsCount: Int = 0,
    progressStopPoint: Float = 0f,
    currentActionType: ActionType = ActionType.RUN,
    isRunning: Boolean = true,
    isFinished: Boolean = false,
    isVisible: Boolean = true,
): SegmentBarState {
    return rememberSaveable(saver = SegmentBarState.Saver) {
        SegmentBarState(
            currentSegmentIndex = currentSegmentIndex,
            segmentsCount = segmentsCount,
            progressStopPoint = progressStopPoint,
            currentActionType = currentActionType,
            isRunning = isRunning,
            isFinished = isFinished,
            isVisible = isVisible,
        )
    }
}

class SegmentBarState constructor(
    currentSegmentIndex: Int = 0,
    segmentsCount: Int = 0,
    progressStopPoint: Float = 0f,
    currentActionType: ActionType = ActionType.RUN,
    isRunning: Boolean = true,
    isFinished: Boolean = false,
    isVisible: Boolean = true,
) {
    var currentSegmentIndex by mutableStateOf(currentSegmentIndex)
    var segmentsCount by mutableStateOf(segmentsCount - 1)
    private var currentAction by mutableStateOf(currentActionType)
    private var isRunning by mutableStateOf(isRunning)
    internal var isFinished by mutableStateOf(isFinished)
    internal var isVisible by mutableStateOf(isVisible)
        private set
    internal var stopPoint by mutableStateOf(progressStopPoint)
        private set

    fun getCurrentActionType(segmentIndex: Int): ActionType = when {
        currentSegmentIndex == segmentIndex -> currentAction
        // reset the segment progress when the PREVIOUS action has been occurred on the next segment
        currentSegmentIndex != segmentsCount
                && currentSegmentIndex == segmentIndex + 1
                && currentAction == ActionType.PREVIOUS -> ActionType.RESET
        else -> ActionType.NONE
    }

    fun saveProgress(segmentIndex: Int, progress: Float) {
        if (currentSegmentIndex == segmentIndex) {
            stopPoint = progress
        }
    }

    fun getProgressStartPoint(segmentIndex: Int): Float =
        when (getCurrentActionType(segmentIndex)) {
            ActionType.NONE -> if (currentSegmentIndex >= segmentIndex) 1f else 0f
            else -> stopPoint
        }

    internal fun moveToNextSegment() {
        if (currentSegmentIndex < segmentsCount) {
            currentSegmentIndex += 1
        }
        currentAction = ActionType.RUN
    }

    internal fun moveToPreviousSegment() {
        if (currentSegmentIndex > 0) {
            currentSegmentIndex -= 1
        }
        currentAction = ActionType.RUN
    }

    fun hide() {
        isVisible = false
    }

    fun show() {
        isVisible = true
    }

    fun run() {
        currentAction = ActionType.RUN
        isRunning = true
    }

    fun stop() {
        currentAction = ActionType.STOP
        isRunning = false
    }

    fun next() {
        currentAction = ActionType.NEXT
        isRunning = true
    }

    fun previous() {
        currentAction = ActionType.PREVIOUS
        isRunning = true
    }

    fun finish() {
        currentAction = ActionType.NONE
        isRunning = false
        isFinished = true
    }

    companion object {
        val Saver: Saver<SegmentBarState, *> = listSaver(save = {
            listOf(
                it.currentSegmentIndex,
                it.segmentsCount,
                it.stopPoint,
                it.currentAction,
                it.isRunning,
                it.isFinished,
                it.isVisible,
            )
        }, restore = {
            SegmentBarState(
                currentSegmentIndex = it[0] as Int,
                segmentsCount = it[1] as Int,
                progressStopPoint = it[2] as Float,
                currentActionType = it[3] as ActionType,
                isRunning = it[4] as Boolean,
                isFinished = it[5] as Boolean,
                isVisible = it[6] as Boolean,
            )
        })
    }
}

enum class ActionType {
    RUN, STOP, NEXT, PREVIOUS, RESET, NONE
}