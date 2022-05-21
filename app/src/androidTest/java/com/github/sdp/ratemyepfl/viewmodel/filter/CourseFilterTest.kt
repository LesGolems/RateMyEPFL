package com.github.sdp.ratemyepfl.viewmodel.filter

import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl.Companion.toCourse
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CourseFilterTest {
    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CourseRepositoryImpl

    private val fake = "fake"
    private val personalizedTeacher = "myPersonalTeacher"
    private val courseBuilder = Course.Builder(
        fake,
        fake,
        fake,
        0,
        fake,
        0.0,
        0,
        fake,
        fake,
        fake,
        fake
    )

    private val title = "title"
    private val courseCode = "courseCode"
    private val personalizedCourse = courseBuilder
        .setTeacher(personalizedTeacher)
        .setTitle(title)
        .setCourseCode(courseCode)
        .build()

    private val courses: List<Course> = listOf(
        personalizedCourse, personalizedCourse.copy(title = "z")
    )

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        courses.forEach {
            runTest { repository.add(it) }
        }
    }

    @After
    fun teardown() {
        courses.forEach {
            runTest { repository.remove(it.getId()) }
        }
    }

    @Test
    fun alphabeticalOrderQueryTest() = runTest {
        CourseFilter.AlphabeticalOrder.toQuery(repository.query())
            .execute(courses.size.toUInt())
            .mapResult { s -> s.mapNotNull { it.toCourse() } }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> {}
                    is QueryState.Success ->
                        assertEquals(it.data, it.data.sortedBy { course -> course.title })
                }
            }
    }

    @Test
    fun alphabeticalOrderReversedQueryTest() = runTest {
        CourseFilter.AlphabeticalOrderReversed.toQuery(repository.query())
            .execute(courses.size.toUInt())
            .mapResult { s -> s.mapNotNull { it.toCourse() } }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> {}
                    is QueryState.Success ->
                        assertEquals(
                            it.data,
                            it.data.sortedBy { course -> course.title }.reversed()
                        )
                }
            }
    }

}