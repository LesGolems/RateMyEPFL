package com.github.sdp.ratemyepfl.ui.fragment.main

import android.widget.SearchView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.TestUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CourseTabFragmentTest {
    @get:Rule
    val hiltAndroidTestRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: CourseRepositoryImpl

    private val cycleBachelor = TestUtils.getString(R.string.cycle_bachelor_field_title)

    private val courses = CourseRepositoryImpl.OFFLINE_COURSES +
            Course(
                title = "Some sv",
                section = "SV",
                teacher = "none",
                credits = 2,
                courseCode = "SV",
                grade = 2.0,
                numReviews = 1,
                cycle = cycleBachelor
            ) +
            Course(
                title = "Some bad course",
                section = "X",
                teacher = "X",
                credits = 2,
                courseCode = "X",
                grade = 0.0,
                numReviews = 0,
                cycle = cycleBachelor
            )

    private fun fillerCourse(x: Int) = Course(
        title = "Filler",
        section = "IC",
        teacher = "Lachowska Anna",
        credits = 6,
        courseCode = "filler$x",
        grade = 2.0,
        numReviews = 1
    )

    private val fillers = (0..10).map { fillerCourse(it) }

    @Before
    fun setUp() {
        hiltAndroidTestRule.inject()
        courses.forEach {
            runTest { repository.add(it).await() }
        }
        fillers.forEach {
            runTest { repository.add(it).await() }
        }

    }

    @After
    fun tearDown() {
        courses.forEach {
            runTest { repository.remove(it.getId()).await() }
        }
        fillers.forEach {
            runTest { repository.remove(it.getId()).await() }
        }

    }

    @Test
    fun startsReviewWhenUserClicksOnCourse() {
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        Thread.sleep(1000)
        init()

        onView(withText(courses.first().toString())).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun searchingForTheAGivenCourseWorks() {
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        CustomViewActions.SearchAction.query("Advanced")
        onView(isAssignableFrom(RecyclerView::class.java))
            .check(matches(hasDescendant(withText(courses.first().toString()))))
    }

//    @Test
//    fun swipeDownLoadMoreItems() {
//        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
//        onView(isAssignableFrom(RecyclerView::class.java))
//            .perform(RecyclerViewActions.scrollToPosition<ReviewableAdapter.ReviewableViewHolder>(10))
//    }

    @Test
    fun searchBarIsCollapsedAtInitialization() {
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        onView(isAssignableFrom(SearchView::class.java))
            .perform(CustomViewActions.createViewAction<SearchView> { _, view ->
                assertEquals(true, (view as SearchView).isIconified)
            })
    }

    @Test
    fun sortAlphabeticallyWorks() {
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        onView(withId(R.id.filterMenuButton)).perform(longClick())
        onView(withText(R.string.sort_by_title)).perform(click())
        onView(withText(TestUtils.getString(R.string.alphabetic_order))).perform(click())

        onView(isAssignableFrom(RecyclerView::class.java)).check(
            matches(
                hasDescendant(
                    withText(
                        courses.minByOrNull { it.title }!!.toString()
                    )
                )
            )
        )
    }

//    @Test
//    fun sortAlphabeticallyReversedWorks() {
//        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
//        onView(ViewMatchers.withId(R.id.filterMenuButton)).perform(longClick())
//        onView(withText(R.string.sort_by_title)).perform(click())
//        onView(withText(TestUtils.getString(R.string.alphabetic_order_decreasing))).perform(click())
//        onView(isAssignableFrom(RecyclerView::class.java))
//            .perform(RecyclerViewActions.scrollToPosition<ReviewableAdapter.ReviewableViewHolder>(0))
//            .check(
//                matches(
//                    hasDescendant(
//                        withText(
//                            courses.maxByOrNull { it.title }!!.toString()
//                        )
//                    )
//                )
//            )
//    }

    @Test
    fun sortByBestRatedWorks() {
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        onView(withId(R.id.filterMenuButton)).perform(longClick())
        onView(withText(R.string.sort_by_title)).perform(click())
        onView(withText(TestUtils.getString(R.string.best_rated_order_title))).perform(click())
        onView(isAssignableFrom(RecyclerView::class.java)).check(
            matches(
                hasDescendant(
                    withText(
                        courses.maxByOrNull { it.grade }!!.toString()
                    )
                )
            )
        )
    }

//    @Test
//    fun sortByWorstRatedWorks() {
//        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
//        onView(ViewMatchers.withId(R.id.filterMenuButton)).perform(longClick())
//        onView(withText(R.string.sort_by_title)).perform(click())
//        onView(withText(TestUtils.getString(R.string.worst_rated_order_title))).perform(click())
//        onView(isAssignableFrom(RecyclerView::class.java)).check(
//            matches(
//                hasDescendant(
//                    withText(
//                        courses.minByOrNull { it.grade }!!.toString()
//                    )
//                )
//            )
//        )
//    }

    @Test
    fun filterBy7CreditsWorks() {
        val credits = 7
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        onView(withId(R.id.filterMenuButton)).perform(longClick())
        onView(withText(R.string.credits)).perform(click())
        onView(withText(credits.toString())).perform(click())
        onView(isAssignableFrom(RecyclerView::class.java)).check(
            matches(
                hasDescendant(
                    withText(
                        courses.first { it.credits == credits }.toString()
                    )
                )
            )
        )
    }

    @Test
    fun filterBySectionSVWorks() {
        val section = "SV"
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        onView(withId(R.id.filterMenuButton)).perform(longClick())
        onView(withText(R.string.section_picker_title)).perform(click())
        onView(withText(TestUtils.getString(R.string.life_sciences_section_title))).perform(click())
        onView(isAssignableFrom(RecyclerView::class.java)).check(
            matches(
                hasDescendant(
                    withText(
                        courses.first { it.section == section }.toString()
                    )
                )
            )
        )
    }

    @Test
    fun filterByCycleBachelorWorks() {
        HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> {}
        onView(withId(R.id.filterMenuButton)).perform(longClick())
        onView(withText(R.string.cycle_picker_title)).perform(click())
        onView(withText(TestUtils.getString(R.string.cycle_bachelor_title))).perform(click())
        onView(isAssignableFrom(RecyclerView::class.java)).check(
            matches(
                hasDescendant(
                    withText(
                        courses.first { it.cycle == cycleBachelor }.toString()
                    )
                )
            )
        )
    }

}