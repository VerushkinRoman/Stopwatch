package com.posse.android.stopwatch.ui.view

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.posse.android.stopwatch.databinding.ActivityMainBinding
import com.posse.android.stopwatch.idlingResource.TestIdlingResource
import com.posse.android.stopwatch.interator.Interator
import com.posse.android.stopwatch.interator.StopwatchStateHolder
import com.posse.android.stopwatch.model.TIMER
import com.posse.android.stopwatch.repository.TimestampProvider
import com.posse.android.stopwatch.ui.viewModel.StopwatchListOrchestrator
import com.posse.android.stopwatch.ui.viewModel.ViewModel
import com.posse.android.stopwatch.utils.ElapsedTimeCalculator
import com.posse.android.stopwatch.utils.StopwatchStateCalculator
import com.posse.android.stopwatch.utils.TimestampMillisecondsFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private var idlingResource: TestIdlingResource? = null

    private val elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider)

    private val interator: Interator = StopwatchStateHolder(
        StopwatchStateCalculator(
            timestampProvider,
            elapsedTimeCalculator
        ),
        elapsedTimeCalculator,
        TimestampMillisecondsFormatter()
    )

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val model: ViewModel = StopwatchListOrchestrator(
        interator,
        scope
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scope.launch { model.ticker.collect { binding.watchText.text = it } }
        scope.launch { model.ticker2.collect { binding.watchText2.text = it } }

        binding.startButton.setOnClickListener { model.start(TIMER.First, idlingResource) }
        binding.pauseButton.setOnClickListener { model.pause(TIMER.First) }
        binding.stopButton.setOnClickListener { model.stop(TIMER.First) }

        binding.startButton2.setOnClickListener { model.start(TIMER.Second, idlingResource) }
        binding.pauseButton2.setOnClickListener { model.pause(TIMER.Second) }
        binding.stopButton2.setOnClickListener { model.stop(TIMER.Second) }
    }

    @VisibleForTesting
    fun getIdlingResource(): TestIdlingResource {
        if (idlingResource == null) {
            idlingResource = TestIdlingResource()
        }
        return idlingResource as TestIdlingResource
    }
}