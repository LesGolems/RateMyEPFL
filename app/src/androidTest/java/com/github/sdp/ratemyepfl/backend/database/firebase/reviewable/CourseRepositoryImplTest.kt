package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.filter.CourseFilter
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
    private val grade = 0.0
    private val numReviews = 0
    private val personalizedTeacher = "myPersonalTeacher"
    private val courseBuilder = Course.Builder(
        fake,
        fake,
        fake,
        0,
        fake,
        grade,
        numReviews,
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
                grade,
                numReviews,
                fake,
                fake,
                fake,
                fake
            )
                .setCourseCode(n.toString())
                .build()
        }.plus(personalizedCourse)

    @Test
    fun filterTest() {
        val f1 = CourseFilter.AlphabeticalOrder
        val f2 = CourseFilter.AlphabeticalOrder

        assertEquals(true, f1.toQuery(repository.query()) == f2.toQuery(repository.query()))
    }

    @Before
    fun setup() {
        hiltRule.inject()
        repository = CourseRepositoryImpl(db)

        val c = courses.map { repository.add(it) }
        c.forEach { runTest { it.await() } }
    }

    @After
    fun teardown() {
        val c = courses.map { repository.remove(it.getId()) }
        c.forEach { runTest { it.await() } }
    }

//    @Test
//    fun searchSucceedForTitleSearch() = runTest {
//        repository.search(CourseRepositoryImpl.TITLE_FIELD_NAME, title)
//            .collect {
//                var x = 0
//                when (it) {
//                    is QueryState.Failure -> throw Exception("Should succeed")
//                    is QueryState.Loading -> x += 1
//                    is QueryState.Success -> {
//                        assertEquals(1, it.data.size)
//                        assertEquals(personalizedCourse, it.data.first())
//                    }
//                }
//
//                if (x == 3) {
//                    throw Exception()
//                }
//            }
//    }
//
//    @Test
//    fun searchSucceedForCourseCodeSearch() = runTest {
//        repository.search(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME, courseCode)
//            .collect {
//                when (it) {
//                    is QueryState.Failure -> throw Exception("Should succeed")
//                    is QueryState.Loading -> {}
//                    is QueryState.Success -> {
//                        assertEquals(personalizedCourse, it.data.first())
//                    }
//                }
//            }
//    }
}