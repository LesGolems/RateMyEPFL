package com.github.sdp.ratemyepfl.fragment.review

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@HiltAndroidTest
class ReviewListFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Test
    fun listIsVisible() {
        HiltUtils.launchFragmentInHiltContainer<ReviewListFragment> {}
        onView(withId(R.id.reviewRecyclerView))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun swipeRefreshes() {
        FakeReviewsRepository.reviewList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .build()
        )
        HiltUtils.launchFragmentInHiltContainer<ReviewListFragment> {}
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
        onView(withId(R.id.reviewRecyclerView))
            .check(ViewAssertions.matches(hasChildCount(1)))
        onView(withId(R.id.reviewSwipeRefresh)).perform(swipeDown())
        onView(withId(R.id.reviewRecyclerView))
            .check(ViewAssertions.matches(CoreMatchers.not(hasChildCount(1))))
    }
}