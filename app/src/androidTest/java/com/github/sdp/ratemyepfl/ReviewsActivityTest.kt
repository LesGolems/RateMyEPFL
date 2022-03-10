package com.github.sdp.ratemyepfl

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class ReviewsActivityTest {
    @Test
    fun isCoursesListViewViewVisibleOnActivityLaunch() {
        onView(withId(R.id.reviewsListView))
                .check(matches(isDisplayed()))
    }
}