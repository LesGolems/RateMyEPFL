package com.github.sdp.ratemyepfl

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.ratemyepfl.activities.classrooms.AddRoomReviewActivity
import com.github.sdp.ratemyepfl.activities.classrooms.ROOM_COMMENT
import com.github.sdp.ratemyepfl.activities.classrooms.ROOM_GRADE
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddRoomReviewActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(AddRoomReviewActivity::class.java)

    @Test
    fun nullGradeCancelsActivity() {
        init()

        val comment = "Good"
        onView(withId(R.id.add_room_comment)).perform(typeText(comment))
        onView(withId(R.id.done_button)).perform(click())

        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nullCommentCancelsActivity() {
        init()

        val grade = "15"
        onView(withId(R.id.add_room_grade)).perform(typeText(grade))
        onView(withId(R.id.done_button)).perform(click())

        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nonNullGradeAndCommentsGivesOK() {
        init()

        val grade = "15"
        val comment = "Good"
        onView(withId(R.id.add_room_grade)).perform(typeText(grade))
        onView(withId(R.id.add_room_comment)).perform(typeText(comment))
        onView(withId(R.id.done_button)).perform(click())

        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_OK))

        release()
    }

    @Test
    fun nonNullGradeAndCommentGivesSameValuesToList() {
        init()

        val grade = "15"
        val comment = "Good"
        onView(withId(R.id.add_room_grade)).perform(typeText(grade))
        onView(withId(R.id.add_room_comment)).perform(typeText(comment))
        onView(withId(R.id.done_button)).perform(click())

        val data = testRule.scenario.result.resultData
        assertThat(data.getStringExtra(ROOM_GRADE), Matchers.equalTo(grade))
        assertThat(data.getStringExtra(ROOM_COMMENT), Matchers.equalTo(comment))

        release()
    }
}