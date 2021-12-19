package com.posse.android.stopwatch

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.posse.android.stopwatch.ui.view.MainActivity
import com.posse.android.stopwatch.ui.viewModel.StopwatchListOrchestrator
import junit.framework.TestCase
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StopwatchActivityEspressoTest {

    private var idlingResource: IdlingResource? = null

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun registerIdlingResource() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            idlingResource = activity.getIdlingResource()
            IdlingRegistry.getInstance().register(idlingResource)
        }
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
            val totalCountTextView = it.findViewById<TextView>(R.id.watchText)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTextView_HasText() {
        val assertion = matches(withText(StopwatchListOrchestrator.DEFAULT_TIME))
        onView(withId(R.id.watchText)).check(assertion)
    }

    @Test
    fun activityTextView_IsDisplayed() {
        onView(withId(R.id.watchText)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun activityTextView_IsCompletelyDisplayed() {
        onView(withId(R.id.watchText)).check(matches(ViewMatchers.isCompletelyDisplayed()))
    }

    @Test
    fun activityButtons_AreEffectiveVisible() {
        onView(withId(R.id.startButton)).check(matches(
            ViewMatchers.withEffectiveVisibility(
                ViewMatchers.Visibility.VISIBLE
            )
        ))
        onView(withId(R.id.startButton2)).check(matches(
            ViewMatchers.withEffectiveVisibility(
                ViewMatchers.Visibility.VISIBLE
            )
        ))
    }

    @Test
    fun activityButtonStart_IsWorking() {
        onView(withId(R.id.startButton)).perform(click())
        onView(withId(R.id.watchText)).check(matches(Matchers.not(withText(StopwatchListOrchestrator.DEFAULT_TIME))))
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        scenario.close()
    }
}