package com.posse.android.stopwatch.ui.viewModel

import com.posse.android.stopwatch.idlingResource.TestIdlingResource
import com.posse.android.stopwatch.model.TIMER
import kotlinx.coroutines.flow.StateFlow

interface ViewModel {

    val ticker2: StateFlow<String>
    val ticker: StateFlow<String>

    fun start(timer: TIMER, idlingResource: TestIdlingResource?)
    fun pause(timer: TIMER)
    fun stop(timer: TIMER)
}