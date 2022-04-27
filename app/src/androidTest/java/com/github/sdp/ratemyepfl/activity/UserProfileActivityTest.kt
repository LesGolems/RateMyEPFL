package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.utils.TestUtils.createImageGallerySetResultStub
import com.github.sdp.ratemyepfl.utils.TestUtils.getActivity
import com.github.sdp.ratemyepfl.utils.TestUtils.savePickedImage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class UserProfileActivityTest {
    lateinit var scenario: ActivityScenario<UserProfileActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Before
    fun setUp(){
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), UserProfileActivity::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean(){
        scenario.close()
    }

    @Test
    fun userProfileVisibleOnLaunch() {
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.modify_profile_button)).check(matches(isDisplayed()))
    }

    @Test
    fun userModificationAreDisabledOnLaunch(){
        onView(withId(R.id.username_text)).check(matches(isNotEnabled()))
        onView(withId(R.id.emailText)).check(matches(isNotEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isNotEnabled()))
    }

    @Test
    fun clickOnModifyButtonEnablesModifications() {
        onView(withId(R.id.modify_profile_button)).perform(click())

        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))

        onView(withId(R.id.username_text)).check(matches(isEnabled()))
        onView(withId(R.id.emailText)).check(matches(isEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isEnabled()))
    }

    @Test
    fun clickOnModifyButtonTwiceDisablesModifications() {
        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.modify_profile_button)).perform(click())

        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))

        onView(withId(R.id.username_text)).check(matches(isNotEnabled()))
        onView(withId(R.id.emailText)).check(matches(isNotEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isNotEnabled()))
    }

    @Test
    fun modifyingUsernameAndEmailWorks() {
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
    }

    @Test
    fun modifyingWithLongUsernameFails() {
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
    }

    @Test
    fun modifyingWithShortUsernameFails() {
        val shortUsername = "a"
        val newEmail = "sasuke.uchiha@gmail.com"

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).perform(typeText(shortUsername))
        closeSoftKeyboard()

        onView(withId(R.id.emailText)).perform(typeText(newEmail))
        closeSoftKeyboard()

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).check((matches(not(withText(shortUsername)))))
    }

    @Test
    fun modifyingWithWrongEmailFormatFails() {
        val newUsername = "Sasuke"
        val newEmail = "sasuke.uchihagmailcom"

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.username_text)).perform(typeText(newUsername))
        closeSoftKeyboard()

        onView(withId(R.id.emailText)).perform(typeText(newEmail))
        closeSoftKeyboard()

        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.emailText)).check(matches(withText(not(newEmail))))
    }

    @Test
    fun changePictureWorks() {
        init()
        val activity = getActivity(scenario)
        savePickedImage(activity, R.raw.pp1)
        val imgGalleryResult = createImageGallerySetResultStub(activity)
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(imgGalleryResult)
        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.modify_profile_image_button)).perform(click())
        release()
    }

    @Test
    fun userLoggedInButNotInDBWorks(){
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        val intent = Intent(ApplicationProvider.getApplicationContext(), UserProfileActivity::class.java)
        val scenario2: ActivityScenario<UserProfileActivity> = ActivityScenario.launch(intent)
        val username = FakeConnectedUser.instance.user!!.username!!.split(" ")[0]
        val email = FakeConnectedUser.instance.user!!.email
        onView(withId(R.id.username_text)).check(matches(withText(username)))
        onView(withId(R.id.emailText)).check(matches(withText(email)))
        scenario2.close()
    }
}