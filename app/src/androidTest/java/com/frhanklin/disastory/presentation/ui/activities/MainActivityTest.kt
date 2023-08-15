package com.frhanklin.disastory.presentation.ui.activities

import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.frhanklin.disastory.utils.DelayIdlingResource
import com.frhanklin.disastory.utils.EspressoIdlingResource
import com.frhanklin.disastory.utils.IdlingResourceUtils
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoIdlingResource())
    }

    @Test
    fun uc01_uc04_ListDisasterInXPeriod_ShowDisasterOnMap() {
        uc01_ListDisasterInXPeriod()
        onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
            ViewActions.click()
        ))
        Thread.sleep(2000)
    }
    @Test
    fun uc05_NotificationAlert() {
        onView(withId(com.frhanklin.disastory.R.id.btn_settings)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.btn_settings)).perform(ViewActions.click())
        onView(withId(com.frhanklin.disastory.R.id.switch_notification)).check(matches(isDisplayed()))
        val notificationSwtichInteraction : ViewInteraction = onView(withId(com.frhanklin.disastory.R.id.switch_notification))
        val currentState = isSwitchChecked(notificationSwtichInteraction)
        if (currentState) {
            onView(withId(com.frhanklin.disastory.R.id.switch_notification)).perform(ViewActions.click())
            onView(withId(com.frhanklin.disastory.R.id.switch_notification)).perform(ViewActions.click())
        } else {
            onView(withId(com.frhanklin.disastory.R.id.switch_notification)).perform(ViewActions.click())
        }

        onView(withId(com.frhanklin.disastory.R.id.btn_back)).perform(ViewActions.click())

        val delayMillis = 5000L
        val delayIdlingResource = DelayIdlingResource(delayMillis)
        IdlingRegistry.getInstance().register(delayIdlingResource)

        IdlingRegistry.getInstance().unregister(delayIdlingResource)
    }
    @Test
    fun uc02_uc03_uc06_uc07_FilterableList_FilterArea_LightAndDarkTheme_AnimationLoading() {
        Intents.init()
        uc02_FilterableList()
        Thread.sleep(2000)
        onView(withId(com.frhanklin.disastory.R.id.btn_settings)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.btn_settings)).perform(ViewActions.click())
//        Intents.intended(IntentMatchers.hasComponent(SettingsActivity::class.java.name))

        onView(withId(com.frhanklin.disastory.R.id.switch_night)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.switch_night)).perform(ViewActions.click())
        onView(withId(com.frhanklin.disastory.R.id.btn_back)).perform(ViewActions.click())
        Thread.sleep(2000)
        uc03_FilterArea()
    }
    @Test
    fun test() {
        onView(withId(com.frhanklin.disastory.R.id.btn_settings)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.btn_settings)).perform(ViewActions.click())
        onView(withId(com.frhanklin.disastory.R.id.switch_night)).check(matches(isDisplayed()))

    }

    private fun checkDisasterList() {
        Thread.sleep(1000)
        onView(withId(com.frhanklin.disastory.R.id.placholder_line)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.placholder_line)).perform(ViewActions.swipeUp())

        Thread.sleep(1000)
        onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        Thread.sleep(1000)
    }


    fun uc01_ListDisasterInXPeriod() {
        val idlingResource = IdlingRegistry.getInstance().resources.firstOrNull()
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        onView(withId(com.frhanklin.disastory.R.id.loading)).check(matches(isDisplayed()))

        Thread.sleep(1000)
        onView(withId(com.frhanklin.disastory.R.id.placholder_line)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.placholder_line)).perform(ViewActions.swipeUp())

        Thread.sleep(1000)
        onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(20))
    }

    fun uc02_FilterableList() {
        val idlingResource = IdlingRegistry.getInstance().resources.firstOrNull()
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
//        onView(withId(com.frhanklin.disastory.R.id.loading)).check(matches(isDisplayed()))

        Thread.sleep(1000)
        onView(withId(com.frhanklin.disastory.R.id.btn_search_flood)).perform(ViewActions.scrollTo())
        onView(withId(com.frhanklin.disastory.R.id.btn_search_flood)).perform(ViewActions.click())
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        checkDisasterList()

        onView(withId(com.frhanklin.disastory.R.id.btn_search_fire)).perform(ViewActions.scrollTo())
        onView(withId(com.frhanklin.disastory.R.id.btn_search_fire)).perform(ViewActions.click())
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        checkDisasterList()


        onView(withId(com.frhanklin.disastory.R.id.btn_search_haze)).perform(ViewActions.scrollTo())
        onView(withId(com.frhanklin.disastory.R.id.btn_search_haze)).perform(ViewActions.click())
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        checkDisasterList()

        onView(withId(com.frhanklin.disastory.R.id.btn_search_flood)).perform(ViewActions.scrollTo())
        onView(withId(com.frhanklin.disastory.R.id.btn_search_flood)).perform(ViewActions.click())
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        checkDisasterList()

        onView(withId(com.frhanklin.disastory.R.id.btn_search_earthquake)).perform(ViewActions.scrollTo())
        onView(withId(com.frhanklin.disastory.R.id.btn_search_earthquake)).perform(ViewActions.click())
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        checkDisasterList()

        onView(withId(com.frhanklin.disastory.R.id.btn_search_wind)).perform(ViewActions.scrollTo())
        onView(withId(com.frhanklin.disastory.R.id.btn_search_wind)).perform(ViewActions.click())
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        checkDisasterList()
    }

    fun uc03_FilterArea() {
        onView(withId(com.frhanklin.disastory.R.id.search_main)).check(matches(isDisplayed()))
        onView(withId(com.frhanklin.disastory.R.id.search_main)).perform(ViewActions.click())
        onView(withId(com.frhanklin.disastory.R.id.list_search_suggestion)).check(matches(isDisplayed()))

        val suggestionText = "Bali"
        onView(AllOf.allOf(withText(suggestionText), isAssignableFrom(TextView::class.java))).perform(ViewActions.click())

        checkDisasterList()
    }

    private fun isSwitchChecked(switchInteraction: ViewInteraction): Boolean {
        var isChecked = false
        switchInteraction.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isEnabled() // Ensure the view is enabled before performing the action
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