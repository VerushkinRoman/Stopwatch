package com.posse.android.stopwatch.interator

import com.posse.android.stopwatch.model.StopwatchState
import com.posse.android.stopwatch.model.TIMER

interface Interator {

    fun start(timer: TIMER)
    fun pause(timer: TIMER)
    fun stop(timer: TIMER)

    fun getCurrentState(timer: TIMER): StopwatchState

    fun getStringTimeRepresentation(timer: TIMER): String
}