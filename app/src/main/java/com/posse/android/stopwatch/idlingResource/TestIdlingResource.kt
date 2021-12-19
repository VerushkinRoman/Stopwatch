package com.posse.android.stopwatch.idlingResource

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback

class TestIdlingResource : IdlingResource {

    private var callback: ResourceCallback? = null

    @Volatile
    private var isIdleNow = true

    override fun getName(): String = this.javaClass.name

    override fun isIdleNow(): Boolean = isIdleNow

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.callback = callback
    }

    fun setIdleState(isIdleNow: Boolean) {
        this.isIdleNow = isIdleNow
        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }
    }
}