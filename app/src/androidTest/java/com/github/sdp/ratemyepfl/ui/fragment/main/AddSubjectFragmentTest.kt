package com.github.sdp.ratemyepfl.ui.fragment.main

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.utils.TestUtils.checkSnackbarText
import com.github.sdp.ratemyepfl.viewmodel.main.AddSubjectViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddSubjectFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<AddSubjectFragment> {}
    }

    @Test
    fun nullTitleDoesNotWork() {
        val comment = "Good"
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        clickOnKindChip("Help")

        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText(comment)))
    }

    @Test
    fun nullCommentDoesNotWork() {
        val title = "Good"
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        clickOnKindChip("Help")

        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewTitle)).check(matches(withText(title)))
    }

    @Test
    fun noKindSelectedDoesNotWork() {
        val title = "Good"
        val comment = "Good"
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()

        onView(withId(R.id.doneButton)).perform(click())
        checkSnackbarText(AddSubjectViewModel.NO_KIND_MESSAGE)
    }

    @Test
    fun tooManyKindsSelectedDoesNotWork() {
        val title = "Good"
        val comment = "Good"
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()

        clickOnKindChip("Food")
        clickOnKindChip("Help")
        checkSnackbarText(AddSubjectViewModel.ONLY_ONE_KIND_MESSAGE)

        onView(withId(R.id.doneButton)).perform(click())
        checkSnackbarText(AddSubjectViewModel.ONLY_ONE_KIND_MESSAGE)
    }

    @Test
    fun nonNullArgumentsAddsSubject() {
        val comment = "Good"
        val title = "Good title"
        val kind = "Food"
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        clickOnKindChip(kind)

        onView(withId(R.id.doneButton)).perform(click())
    }

    @Test
    fun userNotConnectedDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val comment = "Good"
        val title = "Good title"
        val kind = "Food"
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        clickOnKindChip(kind)

        onView(withId(R.id.doneButton)).perform(click())
        checkSnackbarText(DisconnectedUserException.DEFAULT_ERROR_MSG)
    }

    @Test
    fun anonymousSubjectWorks() {
        val comment = "Good"
        val title = "Good title"
        val kind = "Food"
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        clickOnKindChip(kind)
        onView(withId(R.id.anonymous_switch)).perform(click())

        onView(withId(R.id.doneButton)).perform(click())
    }

    private fun clickOnKindChip(kindId: String) {
        onView(allOf(withTagValue(`is`("chip_$kindId" as Any)), isDisplayed())).perform(click())
    }

}