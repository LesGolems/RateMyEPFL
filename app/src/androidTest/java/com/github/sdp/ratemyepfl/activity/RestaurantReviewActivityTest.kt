package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeItemsRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RestaurantReviewActivityTest {
    private lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        FakeItemsRepository.fakeItem = Restaurant("Fake id")
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean(){
        scenario.close()
    }

    @Test
    fun isIdVisibleOnActivityLaunch() {
        onView(withId(R.id.id_restaurant_info))
            .check(matches(ViewMatchers.withText("Fake id")))
    }
}