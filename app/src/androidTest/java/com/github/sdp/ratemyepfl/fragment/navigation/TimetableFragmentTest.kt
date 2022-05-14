package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.TabAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.MainActivity
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
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

    val courses = CourseRepositoryImpl.OFFLINE_COURSES

    val rooms = ClassroomRepositoryImpl.OFFLINE_CLASSROOMS

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        courses.forEach {
            runTest { courseRepo.add(it).await() }
        }
        rooms.forEach{
            runTest { roomRepo.add(it).await() }
        }
    }

    @Test
    fun addClassFilledWorks(){
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())

        // Select course
        onView(withId(R.id.courseClassInputText)).perform(click())
        onView(withId(R.id.courseClassInputText)).perform(click())
        onView(withText(courses.first().toString()))
            .perform(click())

        onView(withId(R.id.courseClassInputText)).check(matches(withText(courses.first().title)))

        // Select room
        onView(withId(R.id.roomClassInputText)).perform(click())
        onView(withId(R.id.roomClassInputText)).perform(click())
        onView(withText(rooms.first().toString()))
            .perform(click())

        onView(withId(R.id.roomClassInputText)).check(matches(withText(rooms.first().name)))

        // Select day
        onView(withId(R.id.dayPicker)).perform(click())

        // Pick times
        onView(withId(R.id.startTimePicker)).perform(CustomViewActions.NumberPickerActions.setNumber(13))
        onView(withId(R.id.endTimePicker)).perform(CustomViewActions.NumberPickerActions.setNumber(13))

        // Submit
        onView(withId(R.id.doneButton)).perform(ViewActions.scrollTo())
        onView(withId(R.id.doneButton)).perform(click())

        // Check that we went back to timetable
        onView(withId(R.id.addClassButton)).check(matches(isDisplayed()))
        scenario.close()
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