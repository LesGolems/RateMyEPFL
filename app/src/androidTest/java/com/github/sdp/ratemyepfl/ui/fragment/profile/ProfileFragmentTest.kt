package com.github.sdp.ratemyepfl.ui.fragment.profile

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.TestUtils.createImageGallerySetResultStub
import com.github.sdp.ratemyepfl.utils.TestUtils.getActivity
import com.github.sdp.ratemyepfl.utils.TestUtils.savePickedImage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProfileFragmentTest {
    lateinit var scenario: ActivityScenario<HiltTestActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        scenario = HiltUtils.launchFragmentInHiltContainer<ProfileFragment> {}
    }

    @Test
    fun userProfileVisibleOnLaunch() {
        onView(withId(R.id.profilePictureLoadingImage)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profilePictureLoadingImage)).check(matches(isDisplayed()))
        onView(withId(R.id.modify_profile_button)).check(matches(isDisplayed()))
    }

    @Test
    fun userModificationAreDisabledOnLaunch() {
        onView(withId(R.id.username_text)).check(matches(isNotEnabled()))
        onView(withId(R.id.emailText)).check(matches(isNotEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isNotEnabled()))
    }

    @Test
    fun clickOnModifyButtonEnablesModifications() {
        onView(withId(R.id.modify_profile_button)).perform(click())

        onView(withId(R.id.profilePictureLoadingImage)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profilePictureLoadingImage)).check(matches(isDisplayed()))

        onView(withId(R.id.username_text)).check(matches(isEnabled()))
        onView(withId(R.id.emailText)).check(matches(isEnabled()))
        onView(withId(R.id.modify_profile_image_button)).check(matches(isEnabled()))
    }

    @Test
    fun clickOnModifyButtonTwiceDisablesModifications() {
        onView(withId(R.id.modify_profile_button)).perform(click())
        onView(withId(R.id.modify_profile_button)).perform(click())

        onView(withId(R.id.profilePictureLoadingImage)).check(matches(isDisplayed()))
        onView(withId(R.id.username_text)).check(matches(isDisplayed()))
        onView(withId(R.id.profilePictureLoadingImage)).check(matches(isDisplayed()))

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
}