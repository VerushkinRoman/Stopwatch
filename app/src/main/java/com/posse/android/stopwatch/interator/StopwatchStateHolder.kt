package com.posse.android.stopwatch.interator

import com.posse.android.stopwatch.model.StopwatchState
import com.posse.android.stopwatch.model.TIMER
import com.posse.android.stopwatch.utils.ElapsedTimeCalculator
import com.posse.android.stopwatch.utils.StopwatchStateCalculator
import com.posse.android.stopwatch.utils.TimestampMillisecondsFormatter

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter
) : Interator {

    private var currentState: StopwatchState = StopwatchState.Paused(0)
    private var currentState2: StopwatchState = StopwatchState.Paused(0)

    override fun start(timer: TIMER) {
        when (timer) {
            TIMER.First -> currentState =
                stopwatchStateCalculator.calculateRunningState(currentState)
            TIMER.Second -> currentState2 =
                stopwatchStateCalculator.calculateRunningState(currentState2)
        }
    }

    override fun pause(timer: TIMER) {
        when (timer) {
            TIMER.First -> currentState =
                stopwatchStateCalculator.calculatePausedState(currentState)
            TIMER.Second -> currentState2 =
                stopwatchStateCalculator.calculatePausedState(currentState2)
        }

    }

    override fun stop(timer: TIMER) {
        when (timer) {
            TIMER.First -> currentState = StopwatchState.Paused(0)
            TIMER.Second -> currentState2 = StopwatchState.Paused(0)
        }

    }

    override fun getCurrentState(timer: TIMER): StopwatchState {
        return when (timer) {
            TIMER.First -> currentState
            TIMER.Second -> currentState2
        }
    }

    override fun getStringTimeRepresentation(timer: TIMER): String {
        val tempState = when (timer) {
            TIMER.First -> currentState
            TIMER.Second -> currentState2
        }
        val elapsedTime = when (tempState) {
            is StopwatchState.Paused -> tempState.elapsedTime
            is StopwatchState.Running -> elapsedTimeCalculator.calculate(tempState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}