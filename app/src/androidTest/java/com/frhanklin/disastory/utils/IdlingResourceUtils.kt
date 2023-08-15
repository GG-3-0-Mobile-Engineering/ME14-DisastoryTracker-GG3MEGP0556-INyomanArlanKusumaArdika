package com.frhanklin.disastory.utils

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object IdlingResourceUtils {
    fun waitForIdlingResource(idlingResource: IdlingResource, timeout: Long = 10, unit: TimeUnit = TimeUnit.SECONDS) {
        val latch = CountDownLatch(1)

        val idlingCallback = object : IdlingResource.ResourceCallback {
            override fun onTransitionToIdle() {
                latch.countDown()
            }

            fun onIdle() {
                latch.countDown()
            }
        }

        idlingResource.registerIdleTransitionCallback(idlingCallback)

        if (!idlingResource.isIdleNow) {
            // Wait for the latch to count down or until the specified timeout
            latch.await(timeout, unit)
        }

        // Unregister the callback to prevent leaks
//        idlingResource.unregisterIdleTransitionCallback(idlingCallback)
        IdlingRegistry.getInstance().unregister(idlingResource)
    }


}

class DelayIdlingResource(private val delayMillis: Long) : IdlingResource {

    private var startTime: Long = System.currentTimeMillis()
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "DelayIdlingResource"
    }

    override fun isIdleNow(): Boolean {
        val elapsedTime = System.currentTimeMillis() - startTime
        val idle = elapsedTime >= delayMillis

        if (idle) {
            callback?.onTransitionToIdle()
        }

        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }
}
