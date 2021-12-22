package com.posse.android.stopwatch

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.posse.android.stopwatch.ui.view.MainActivity
import com.posse.android.stopwatch.ui.viewModel.StopwatchListOrchestrator
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class StopwatchActivityTest {

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val watch1Text = it.findViewById<TextView>(R.id.watchText)
            TestCase.assertNotNull(watch1Text)
        }
    }


    @Test
    fun activityTextView_HasText() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.watchText)
            TestCase.assertEquals(StopwatchListOrchestrator.DEFAULT_TIME, totalCountTextView.text)
        }
    }

    @Test
    fun activityTextView_IsVisible() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.watchText)
            TestCase.assertEquals(View.VISIBLE, totalCountTextView.visibility)
        }
    }

    @Test
    fun activityStartButtons_AreVisible() {
        scenario.onActivity {
            val start1Button = it.findViewById<Button>(R.id.startButton)
            TestCase.assertEquals(View.VISIBLE, start1Button.visibility)

            val start2Button = it.findViewById<Button>(R.id.startButton2)
            TestCase.assertEquals(View.VISIBLE, start2Button.visibility)
        }
    }

    @Test
    fun activityButtonStart_IsWorking() {
        scenario.onActivity {
            testCoroutineRule.runBlockingTest {
                val start1Button = it.findViewById<Button>(R.id.startButton)
                val watch1Text = it.findViewById<TextView>(R.id.watchText)
                start1Button.performClick()
                delay(500)
                TestCase.assertNotSame(StopwatchListOrchestrator.DEFAULT_TIME, watch1Text.text)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}