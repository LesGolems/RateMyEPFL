package com.github.sdp.ratemyepfl.ui.fragment.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeImageStorage
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.TestUtils
import com.github.sdp.ratemyepfl.utils.TestUtils.withDrawable
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RankingFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        HiltUtils.launchFragmentInHiltContainer<RankingFragment> { }
    }

    @Test
    fun podiumIsDisplayed() {
        onView(withId(R.id.podium)).check(matches(isDisplayed()))
    }

    @Test
    fun correctUsersAreDisplayed() {
        val user1 = FakeUserRepository.userMap[FakeUserRepository.UID1]!!
        val user2 = FakeUserRepository.userMap[FakeUserRepository.UID2]!!
        val user3 = FakeUserRepository.userMap[FakeUserRepository.UID3]!!

        FakeImageStorage.images[FakeUserRepository.UID1] = ImageFile(
            "${FakeUserRepository.UID1}.jpg",
            TestUtils.resourceToBitmap(R.raw.fake_profile_picture)
        )

        FakeImageStorage.images[FakeUserRepository.UID2] = ImageFile(
            "${FakeUserRepository.UID2}.jpg",
            TestUtils.resourceToBitmap(R.raw.fake_profile_picture)
        )

        FakeImageStorage.images[FakeUserRepository.UID3] = ImageFile(
            "${FakeUserRepository.UID3}.jpg",
            TestUtils.resourceToBitmap(R.raw.fake_profile_picture)
        )

        onView(withId(R.id.podiumSwipeRefresh)).perform(swipeDown())

        Thread.sleep(2000)

        onView(withId(R.id.userTop1Name)).check(matches(withText(user1.username)))
        onView(withId(R.id.userTop1Karma)).check(matches(withText(user1.karma.toString())))
        onView(withId(R.id.userTop1Picture)).check(matches(withDrawable(R.raw.fake_profile_picture)))

        onView(withId(R.id.userTop2Name)).check(matches(withText(user2.username)))
        onView(withId(R.id.userTop2Karma)).check(matches(withText(user2.karma.toString())))
        onView(withId(R.id.userTop2Picture)).check(matches(withDrawable(R.raw.fake_profile_picture)))

        onView(withId(R.id.userTop3Name)).check(matches(withText(user3.username)))
        onView(withId(R.id.userTop3Karma)).check(matches(withText(user3.karma.toString())))
        onView(withId(R.id.userTop3Picture)).check(matches(withDrawable(R.raw.fake_profile_picture)))
    }
}