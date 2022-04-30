package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.FirebaseFirestore
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
class CourseRepositoryImplTest {
    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: CourseRepositoryImpl

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    private val fake = "fake"
    private val fakeTeacher = fake
    private val personalizedTeacher = "myPersonalTeacher"
    private val courseBuilder = Course.Builder(
        fake,
        fake,
        fake,
        0,
        fake,
        0,
        0.0,
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

    private val courses: List<Course> = (0..30)
        .map { n ->
            Course.Builder(
                fake,
                fake,
                fake,
                0,
                fake,
                0,
                0.0,
                fake,
                fake,
                fake,
                fake
            )
                .setCourseCode(n.toString())
                .setNumReviews(n % 5 + 1)
                .setAverageGrade(((n + 2) % 5 + 1).toDouble())
                .build()
        }.plus(personalizedCourse)

    @Before
    fun setup() {
        hiltRule.inject()
        repository = CourseRepositoryImpl(db)

        val c = courses.map { repository.add(it) }
        c.forEach { runTest { it.await() } }
    }

    @After
    fun teardown() = runTest {
        repository.getCourses()
            .map { it.getId() }
            .forEach { repository.remove(it).await() }
    }

    @Test
    fun searchSucceedForTitleSearch() = runTest {
        repository.search(title)
            .collect {
                var x = 0
                when (it) {
                    is QueryState.Failure -> throw Exception("Should succeed")
                    is QueryState.Loading -> x += 1
                    is QueryState.Success -> {
                        assertEquals(1, it.data.size)
                        assertEquals(personalizedCourse, it.data.first())
                    }
                }

                if (x == 3) {
                    throw Exception()
                }
            }
    }

    @Test
    fun searchSucceedForCourseCodeSearch() = runTest {
        repository.search(courseCode)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(personalizedCourse, it.data.first())
                    }
                }
            }
    }
}