package com.github.sdp.ratemyepfl.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import com.github.sdp.ratemyepfl.activity.MainActivity
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.TabAction
import com.github.sdp.ratemyepfl.utils.clickOnViewChild
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TimetableFragmentTest {
    lateinit var scenario: ActivityScenario<HiltTestActivity>

    @get:Rule
    val hiltAndroidRule: HiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var courseRepo: CourseRepositoryImpl

    @Inject
    lateinit var roomRepo: ClassroomRepositoryImpl

    private val courses = CourseRepositoryImpl.OFFLINE_COURSES

    private val rooms = ClassroomRepositoryImpl.OFFLINE_CLASSROOMS

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        courses.forEach {
            runTest { courseRepo.add(it).await() }
        }
        rooms.forEach {
            runTest { roomRepo.add(it).await() }
        }
    }

    @After
    fun tearDown() {
        courses.forEach {
            runTest { courseRepo.remove(it.getId()).await() }
        }
        rooms.forEach {
            runTest { roomRepo.remove(it.getId()).await() }
        }

    }

    private fun selectCourse() {
        onView(withId(R.id.courseClassInputText)).perform(click())
        onView(withId(R.id.courseClassInputText)).perform(click())
        onView(withText(courses.first().toString()))
            .perform(click())
    }

    private fun selectRoom() {
        onView(withId(R.id.roomClassInputText)).perform(click())
        onView(withId(R.id.roomClassInputText)).perform(click())
        onView(withText(rooms.first().toString()))
            .perform(click())
    }

    @Test
    fun addClassFilledWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())

        selectCourse()
        onView(withId(R.id.courseClassInputText)).check(matches(withText(courses.first().title)))

        selectRoom()
        onView(withId(R.id.roomClassInputText)).check(matches(withText(rooms.first().name)))

        // Select day
        onView(withId(R.id.dayPicker)).perform(click())

        // Pick times
        onView(withId(R.id.startTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )
        onView(withId(R.id.endTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )

        // Submit
        onView(withId(R.id.doneButton)).perform(ViewActions.scrollTo())
        onView(withId(R.id.doneButton)).perform(click())

        // Check that we went back to timetable
        onView(withId(R.id.addClassButton)).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun addClassMissingCourseFails() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())

        selectRoom()

        // Select day
        onView(withId(R.id.dayPicker)).perform(click())

        // Pick times
        onView(withId(R.id.startTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )
        onView(withId(R.id.endTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )

        // Submit
        onView(withId(R.id.doneButton)).perform(ViewActions.scrollTo())
        onView(withId(R.id.doneButton)).perform(click())

        // Check that we still are in add class fragment
        onView(withId(R.id.addClassTitle)).perform(ViewActions.scrollTo())
        onView(withId(R.id.addClassTitle)).check(matches(isDisplayed()))
        scenario.close()
    }


    @Test
    fun addClassMissingRoomFails() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())

        selectCourse()

        // Select day
        onView(withId(R.id.dayPicker)).perform(click())

        // Pick times
        onView(withId(R.id.startTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )
        onView(withId(R.id.endTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )

        // Submit
        onView(withId(R.id.doneButton)).perform(ViewActions.scrollTo())
        onView(withId(R.id.doneButton)).perform(click())

        // Check that we still are in add class fragment
        onView(withId(R.id.addClassTitle)).perform(ViewActions.scrollTo())
        onView(withId(R.id.addClassTitle)).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun addClassMissingDayFails() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())

        selectCourse()

        selectRoom()

        // Select day
        onView(withId(R.id.dayPicker)).perform(click())

        // Deselect day
        onView(withId(R.id.dayPicker)).perform(click())

        // Pick times
        onView(withId(R.id.startTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )
        onView(withId(R.id.endTimePicker)).perform(
            CustomViewActions.NumberPickerActions.setNumber(
                13
            )
        )

        // Submit
        onView(withId(R.id.doneButton)).perform(ViewActions.scrollTo())
        onView(withId(R.id.doneButton)).perform(click())

        // Check that we still are in add class fragment
        onView(withId(R.id.addClassTitle)).perform(ViewActions.scrollTo())
        onView(withId(R.id.addClassTitle)).check(matches(isDisplayed()))
        scenario.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun buttonNotShownWhenLoggedOut() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        scenario = HiltUtils.launchFragmentInHiltContainer<TimetableFragment> { }
        onView(withId(R.id.addClassButton)).check(matches(not(isDisplayed())))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun goesToReviewWhenClickOnCourseName() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val b = Bundle()
        val t = arrayListOf(
            Class("CS-306", "Bamboula", "René", "CM3", 0, 10, 12)
        )
        b.putString("day", "Monday")
        b.putSerializable("timetable", t)

        scenario = HiltUtils.launchFragmentInHiltContainer<DayFragment>(b)

        init()

        onView(withId(R.id.classRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ReviewAdapter.ReviewViewHolder>(
                0,
                clickOnViewChild(R.id.classname)
            )
        )
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun goesToReviewWhenClickOnRoom() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val b = Bundle()
        val t = arrayListOf(
            Class("CS-306", "Bamboula", "René", "CM3", 0, 10, 12)
        )
        b.putString("day", "Monday")
        b.putSerializable("timetable", t)

        scenario = HiltUtils.launchFragmentInHiltContainer<DayFragment>(b)

        init()

        onView(withId(R.id.classRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ReviewAdapter.ReviewViewHolder>(
                0,
                clickOnViewChild(R.id.room)
            )
        )
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsDayFragment() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        scenario = HiltUtils.launchFragmentInHiltContainer<TimetableFragment> { }
        TabAction.selectTab(DayFragment.DAYS.MONDAY.day)
        checkDay(0)
    }

    private fun checkDay(day: Int) {
        onView(
            withText(
                FakeUserRepository.timetable[0].name
            )
        ).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}