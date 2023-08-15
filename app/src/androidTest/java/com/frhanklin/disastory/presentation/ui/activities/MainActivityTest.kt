package com.frhanklin.disastory.presentation.ui.activities

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.frhanklin.disastory.utils.DummyData
import com.frhanklin.disastory.utils.EspressoIdlingResource
import com.frhanklin.disastory.utils.IdlingResourceUtils
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    private val dummyDisasters = DummyData.getDummyDisaster()

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
    fun `uc01_ListDisasterInXPeriod`() {
        val idlingResource = IdlingRegistry.getInstance().resources.firstOrNull()
        idlingResource?.let {
            IdlingResourceUtils.waitForIdlingResource(it)
        }
        Espresso.onView(withId(com.frhanklin.disastory.R.id.loading)).check(matches(isDisplayed()))

        Thread.sleep(1000)
        Espresso.onView(withId(com.frhanklin.disastory.R.id.placholder_line)).check(matches(isDisplayed()))
        Espresso.onView(withId(com.frhanklin.disastory.R.id.placholder_line)).perform(ViewActions.swipeUp())

        Thread.sleep(1000)
        Espresso.onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).check(matches(isDisplayed()))
        Espresso.onView(withId(com.frhanklin.disastory.R.id.rv_disaster_list)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
        Thread.sleep(1000)



//        Espresso.onView(withId(com.frhanklin.disastory.R.id.bottom_sheet)).check(matches(isDisplayed()))
//        onView(withId(com.frhanklin.disastory.R.id.bottom_sheet)).perform(ViewActions.swipeUp())
    }

    @Test
    fun `uc02_FilterableList`() {

    }
    @Test
    fun `uc03_FilterArea`() {

    }
    @Test
    fun `uc04_ShowDisasterOnMap`() {

    }
    @Test
    fun `uc04_NotificationAlert`() {

    }
    @Test
    fun `uc04_LightAndDarkTheme`() {

    }
    @Test
    fun `uc07_AnimationLoading`() {

    }
}