package com.frhanklin.disastory.presentation.ui.activities

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.widget.Switch
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(SettingsActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(SettingsActivity::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testNightSwitch() {
        onView(withId(com.frhanklin.disastory.R.id.switch_night)).check(matches(isDisplayed()))
        val nightSwitchInteraction : ViewInteraction = onView(withId(com.frhanklin.disastory.R.id.switch_night))
        val currentState = isSwitchChecked(nightSwitchInteraction)

        val uiModeManager = ApplicationProvider.getApplicationContext<Context>().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isNightMode = uiModeManager.nightMode == Configuration.UI_MODE_NIGHT_YES

        if (currentState) {
            nightSwitchInteraction.perform(click())
            assertTrue(isNightMode)
        } else {
            nightSwitchInteraction.perform(click())
            assertTrue(!isNightMode)
        }
    }

    private fun isSwitchChecked(switchInteraction: ViewInteraction): Boolean {
        var isChecked = false
        switchInteraction.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isEnabled() // Ensure the view is enabled before performing the action
            }

            override fun getDescription(): String {
                return "Check switch state"
            }

            override fun perform(uiController: UiController?, view: View?) {
                isChecked = (view as? Switch)?.isChecked == true
            }
        })
        return isChecked
    }


}