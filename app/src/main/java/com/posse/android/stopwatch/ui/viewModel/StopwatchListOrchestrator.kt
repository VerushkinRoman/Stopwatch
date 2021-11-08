package com.posse.android.stopwatch.ui.viewModel

import com.posse.android.stopwatch.interator.Interator
import com.posse.android.stopwatch.model.TIMER
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchListOrchestrator(
    private val interator: Interator,
    private val scope: CoroutineScope,
) : ViewModel {

    private var tickerJob: Job? = null
    private var ticker2Job: Job? = null
    private val mutableTicker = MutableStateFlow(DEFAULT_TIME)
    private val mutableTicker2 = MutableStateFlow(DEFAULT_TIME)

    override val ticker: StateFlow<String> = mutableTicker

    override val ticker2: StateFlow<String> = mutableTicker2

    override fun start(timer: TIMER) {
        when (timer) {
            TIMER.First -> {
                if (tickerJob == null) startJob(timer)
            }
            TIMER.Second -> {
                if (ticker2Job == null) startJob(timer)
            }
        }
        interator.start(timer)
    }

    override fun pause(timer: TIMER) {
        interator.pause(timer)
        stopJob(timer)
    }

    override fun stop(timer: TIMER) {
        interator.stop(timer)
        stopJob(timer)
        clearValue(timer)
    }

    private fun startJob(timer: TIMER) {
        when (timer) {
            TIMER.First -> {
                tickerJob = scope.launch {
                    while (isActive) {
                        mutableTicker.value = interator.getStringTimeRepresentation(timer)
                        delay(20)
                    }
                }
            }
            TIMER.Second -> {
                ticker2Job = scope.launch {
                    while (isActive) {
                        mutableTicker2.value = interator.getStringTimeRepresentation(timer)
                        delay(20)
                    }
                }
            }
        }
    }

    private fun stopJob(timer: TIMER) {
        when (timer) {
            TIMER.First -> {
                tickerJob?.cancel()
                tickerJob = null
            }
            TIMER.Second -> {
                ticker2Job?.cancel()
                ticker2Job = null
            }
        }

    }

    private fun clearValue(timer: TIMER) {
        when (timer) {
            TIMER.First -> mutableTicker.value = DEFAULT_TIME
            TIMER.Second -> mutableTicker2.value = DEFAULT_TIME
        }
    }

    companion object {
        const val DEFAULT_TIME = "00:00:000"
    }
}