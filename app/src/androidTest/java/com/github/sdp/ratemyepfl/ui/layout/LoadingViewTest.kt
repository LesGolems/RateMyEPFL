package com.github.sdp.ratemyepfl.ui.layout

import android.app.Activity
import android.graphics.ColorSpace.match
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoadingViewTest {

    private lateinit var loadingView: LoadingView<TextView>

    @get:Rule
    val activityScenarioRule: ActivityScenarioRule<TestActivity> = ActivityScenarioRule(TestActivity::class.java)

    @Test
    fun displayLoadingWhenResultIsNotComplete() {
        activityScenarioRule.scenario.onActivity {
            val loading = flow {
                Espresso.onView(withId(R.id.testProgressBar)).check(matches(isDisplayed()))
                emit(0)
            }
            runBlocking {
                it.loadingView.display(loading, { res ->
                    assertEquals(0, res)
                    onView(withId(R.id.testLoadingView)).check(matches(isDisplayed()))
                })
            }
        }
    }

    @Test
    fun makeOnError() {
        activityScenarioRule.scenario.onActivity {
            var error = false
            runBlocking {
                it.loadingView.display(flow<Int> { throw Exception() }, {}) {
                    error = true
                }
            }

            assertEquals(true, error)
        }
    }

    class TestActivity: Activity() {
        lateinit var loadingView: LoadingView<TextView>
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.layout_test_loading_view)
            val p = findViewById<ProgressBar>(R.id.testProgressBar)
            val view = findViewById<TextView>(R.id.testLoadingView)
            loadingView = LoadingView(view, p)
        }

    }
}