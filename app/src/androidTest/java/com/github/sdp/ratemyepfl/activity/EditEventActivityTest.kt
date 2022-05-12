package com.github.sdp.ratemyepfl.activity

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class EditEventActivityTest {
    lateinit var scenario: ActivityScenario<EditEventActitivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    /**
    @Before
    fun setUp() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, R.menu.bottom_navigation_menu_course_review) // can be any
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_course_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun clickOnDoneBeforeFillingDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnDoneHavingFilledOnlyNameDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.editEventName)).perform(typeText("Truc de fou"))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnDoneLoggedOutDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnCancelButtonWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        init()
        onView(withId(R.id.cancelButton)).perform(scrollTo())
        onView(withId(R.id.cancelButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun swipeOnTheFillersWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.limitPicker)).perform(swipeDown())
        onView(withId(R.id.datePicker)).perform(swipeDown())
        onView(withId(R.id.timePicker)).perform(scrollTo())
        onView(withId(R.id.timePicker)).perform(swipeDown())
        onView(withId(R.id.timePicker)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMapWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.mapContainer)).perform(scrollTo())
        onView(withId(R.id.mapContainer)).perform(swipeUp())
        onView(withId(R.id.mapContainer)).perform(click())
        onView(withId(R.id.mapContainer)).check(matches(isDisplayed()))
    }

    @Test
    fun submittingWhenFilledWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditEventActitivity::class.java)
        intent.putExtra(EditEventActitivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.mapContainer)).perform(scrollTo())
        onView(withId(R.id.mapContainer)).perform(swipeUp())
        onView(withId(R.id.mapContainer)).perform(click())
        onView(withId(R.id.mapContainer)).check(matches(isDisplayed()))
    }
    */
}