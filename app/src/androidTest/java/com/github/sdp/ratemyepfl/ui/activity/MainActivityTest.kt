package com.github.sdp.ratemyepfl.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.utils.CustomViewActions.navigateTo
import com.github.sdp.ratemyepfl.utils.TestUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {
    private val providerName = LocationManager.GPS_PROVIDER
    private lateinit var mContext: Context
    private lateinit var mManager: LocationManager
    lateinit var scenario: ActivityScenario<MainActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun updateLocation(latitude: Double, longitude: Double) {
        val location = Location(providerName)
        location.latitude = latitude
        location.longitude = longitude
        location.accuracy = 1.0f
        location.time = System.currentTimeMillis()
        location.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        mManager.setTestProviderLocation(providerName, location)
    }

    // 'a' to run the test first
    @Test
    fun allocationWorks() {
        mContext = getInstrumentation().targetContext
        mManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mManager.addTestProvider(
            providerName, true, //requiresNetwork,
            false, // requiresSatellite,
            true,  // requiresCell,
            false, // hasMonetaryCost,
            false, // supportsAltitude,
            false, // supportsSpeed,
            false, // supportsBearing,
            Criteria.POWER_MEDIUM, // powerRequirement
            Criteria.ACCURACY_FINE
        ) // accuracy
        mManager.setTestProviderEnabled(providerName, true)


        FakeRestaurantRepository.occupancyCounter = 0
        Thread.sleep(2000)
        updateLocation(46.519214, 6.567553)
        Thread.sleep(500)
        assertEquals(1, FakeRestaurantRepository.occupancyCounter)
        Thread.sleep(1500)
        updateLocation(46.520625, 6.569403)
        Thread.sleep(500)
        assertEquals(1, FakeRestaurantRepository.occupancyCounter)
        Thread.sleep(1500)
        updateLocation(5.0, 6.0)
        Thread.sleep(500)
        assertEquals(0, FakeRestaurantRepository.occupancyCounter)

        mManager.removeTestProvider(providerName)
    }

    @Test
    fun displayTheCorrectTextWhenUserIsNotLoggedIn() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val username = "Visitor"
        val email = "You are not logged in"
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.usernameDrawer)).check(matches(withText(username)))
        onView(withId(R.id.emailDrawer)).check(matches(withText(email)))
        scenario.close()
    }

    @Test
    fun displayTheCorrectTextWhenUserIsLoggedIn() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val username = FakeConnectedUser.Instance.FAKE_USER_1.user!!.username
        val email = FakeConnectedUser.Instance.FAKE_USER_1.user.email
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.usernameDrawer)).check(matches(withText(username)))
        onView(withId(R.id.emailDrawer)).check(matches(withText(email)))
        scenario.close()
    }

    @Test
    fun updateToLoginWhenUserPressesLogout() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.logout)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.login)).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun loginGoesToSplashScreen() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        init()
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.login)).perform(click())
        intended(hasComponent("com.github.sdp.ratemyepfl.ui.activity.SplashScreen"))
        release()
        scenario.close()
    }

    @Test
    fun updateToVisitorWhenUserPressesLogout() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.logout)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.usernameDrawer)).check(matches(withText("Visitor")))
        scenario.close()
    }

    @Test
    fun navigateToProfileWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.profile)).perform(click())
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToTimetableWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.timetableTabLayout)).check(matches(isDisplayed()))
    }

    //    /**
//     * Template test to a BottomNavigationView
//     * The old style with supportFragmentManager does not work since we do not use the
//     * manager anymore
//     */
    @Test
    fun navigateHomePageAddsTheCorrectFragment() {
        navigate(Destination.REVIEW)
        navigate(Destination.HOME)
        checkHomePage()
    }

    //
    @Test
    fun pressesBackReturnsToHome() {
        navigate(Destination.REVIEW)
        navigate(Destination.EVENT)
        navigate(Destination.MAP)
        Espresso.pressBack()
        checkHomePage()
    }

    private fun checkHomePage() = checkPage(Destination.HOME)
    private fun checkReviewPage() = checkPage(Destination.REVIEW)
    private fun checkEventPage() = checkPage(Destination.EVENT)
    private fun checkMapPage() = checkPage(Destination.MAP)

    private fun checkPage(destination: Destination) =
        onView(first(withText(destination.getString()))).check(
            matches(isDisplayed())
        )

    private fun navigate(destination: Destination): ViewInteraction =
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(destination.id))

    enum class Destination(val id: Int, private val stringId: Int) {
        HOME(R.id.home, R.string.home_nav_title),
        REVIEW(R.id.review, R.string.reviews_nav_title),
        EVENT(R.id.event, R.string.event_page_title),
        MAP(R.id.map, R.string.map_nav_title);

        fun getString() = TestUtils.getString(stringId)
    }


    //
    @Test
    fun navigateToReviewAddsTheCorrectFragment() {
        navigate(Destination.REVIEW)
        checkReviewPage()
    }

    @Test
    fun navigateToEventPageAddsTheCorrectFragment() {
        navigate(Destination.EVENT)
        checkEventPage()
    }

    @Test
    fun navigateToMapPageAddsTheCorrectFragment() {
        navigate(Destination.MAP)
        checkMapPage()
    }

    private fun <T> first(matcher: Matcher<T>): Matcher<T> {
        return object : BaseMatcher<T>() {
            var isFirst = true
            override fun matches(item: Any): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }

}