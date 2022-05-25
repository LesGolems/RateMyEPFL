package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.backend.database.util.RepositoryUtil
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Integer.min
import javax.inject.Inject

private typealias ReviewableItem = Course

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class ReviewableRepositoryImplTest {

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: ReviewableRepository<ReviewableItem>

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    private val fake = "fake"
    private val fakeTeacher = fake
    private val grade = 0.0
    private val numReviews = 0
    private val personalizedTeacher = "myPersonalTeacher2.0"
    private val courseBuilder =
        Course(fake, fake, fake, 0, fake, grade, numReviews, fake, fake, fake, fake)

    private val personalizedCourse = courseBuilder.copy(teacher = personalizedTeacher)

    private val courses: List<ReviewableItem> = (0..30)
        .map { n ->
            Course(fake, fake, fake, 0, fake, grade, numReviews, fake, fake, fake, fake)
                .copy(courseCode = n.toString())
        }.plus(personalizedCourse)

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        repository = CourseRepositoryImpl(db)
        val c = courses.map { repository.add(it) }
        c.forEach { it.collect() }
    }

    @After
    fun teardown() {
        RepositoryUtil.clear(db.collection(CourseRepositoryImpl.COURSE_COLLECTION_PATH))
    }

    @Test
    fun searchWorksForAFullMatch() = runTest {
        onTeacherSearch(personalizedTeacher, 1) {
            //assertEquals(personalizedCourse, it.first())
            assertEquals(1, it.size)
        }
    }

    @Test
    fun searchWorksForPrefixMatch() = runTest {
        onTeacherSearch("f", 31) {
            it.forEach { course -> assertEquals(fakeTeacher, course.teacher) }
        }
    }

    @Test
    fun loadSubsequentDataWorks() = runTest {
        val sortedCourses = courses.sortedBy {
            it.courseCode
        }
        val query = repository.query()
            .orderBy(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME)
        repository.load(query, 4u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(sortedCourses.take(4), it.data)
                }
            }

        repository.load(query, 3u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(sortedCourses.take(7), it.data)
                }
            }
    }

    private suspend fun onTeacherSearch(
        name: String,
        numberOfMatch: Int,
        action: (List<Course>) -> Unit
    ) =
        repository.search(CourseRepositoryImpl.TEACHER_FIELD_NAME, name)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Should not fail")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        action(it.data)
                        assertEquals(
                            min(ReviewableRepository.LIMIT_QUERY_SEARCH.toInt(), numberOfMatch),
                            it.data.size
                        )
                    }
                }

            }

}