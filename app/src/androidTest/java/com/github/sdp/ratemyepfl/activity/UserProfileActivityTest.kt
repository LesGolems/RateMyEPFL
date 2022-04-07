package com.github.sdp.ratemyepfl.activity


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class UserProfileActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(UserProfileActivity::class.java)

    @Test
    fun userProfileVisibleOnLaunch() {
        init()
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.modify_profile_button)).check(matches(isDisplayed()))
        release()
    }

    @Test
    fun userModificationAreDisabledOnLaunch(){
        init()
        onView(withId(R.id.username_text)).check(matches(isNotEnabled()))
        onView(withId(R.id.emailText)).check(matches(isNotEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isNotEnabled()))
        release()
    }

    @Test
    fun clickOnModifyButtonEnablesModifications() {
        init()
        onView(withId(R.id.modify_profile_button)).perform(click())

        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))

        onView(withId(R.id.username_text)).check(matches(isEnabled()))
        onView(withId(R.id.emailText)).check(matches(isEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isEnabled()))

        release()
    }

    @Test
    fun clickOnModifyButtonTwiceDisablesModifications() {
        init()
        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.modify_profile_button)).perform(click())

        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))

        onView(withId(R.id.username_text)).check(matches(isNotEnabled()))
        onView(withId(R.id.emailText)).check(matches(isNotEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isNotEnabled()))

        release()
    }

    @Test
    fun modifyingUsernameAndEmailWorks() {
        init()
        val newUsername = "Sasuke"
        val newEmail = "sasuke.uchiha@gmail.com"

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).perform(typeText(newUsername))
        closeSoftKeyboard()

        onView(withId(R.id.emailText)).perform(typeText(newEmail))
        closeSoftKeyboard()

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).check(matches(withText(newUsername)))
        onView(withId(R.id.emailText)).check(matches(withText(newEmail)))

        release()
    }

    @Test
    fun modifyingWithLongUsernameFails() {
        init()
        val longUsername = "usernameThatIsWayTooLong"
        val newEmail = "sasuke.uchiha@gmail.com"

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).perform(typeText(longUsername))
        closeSoftKeyboard()

        onView(withId(R.id.emailText)).perform(typeText(newEmail))
        closeSoftKeyboard()

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).check((matches(not(withText(longUsername)))))
        onView(withId(R.id.emailText)).check(matches(withText(not(newEmail))))

        release()
    }

    @Test
    fun modifyingWithShortUsernameFails() {
        init()
        val shortUsername = "a"
        val newEmail = "sasuke.uchiha@gmail.com"

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).perform(typeText(shortUsername))
        closeSoftKeyboard()

        onView(withId(R.id.emailText)).perform(typeText(newEmail))
        closeSoftKeyboard()

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).check((matches(not(withText(shortUsername)))))

        release()
    }

    @Test
    fun modifyingWithWrongEmailFormatFails() {
        init()
        val newUsername = "Sasuke"
        val newEmail = "sasuke.uchihagmailcom"

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).perform(typeText(newUsername))
        closeSoftKeyboard()

        onView(withId(R.id.emailText)).perform(typeText(newEmail))
        closeSoftKeyboard()

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.emailText)).check(matches(withText(not(newEmail))))

        release()
    }
}