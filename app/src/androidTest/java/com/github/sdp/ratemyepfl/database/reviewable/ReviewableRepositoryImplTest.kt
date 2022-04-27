package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.SearchableRepository
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl.Companion.toCourse
import com.github.sdp.ratemyepfl.database.util.RepositoryUtil
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Integer.min
import javax.inject.Inject

private typealias ReviewableItem = Course

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ReviewableRepositoryImplTest {

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: ReviewableRepositoryImpl<ReviewableItem>

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

    private val personalizedCourse = courseBuilder.setTeacher(personalizedTeacher).build()

    private val courses: List<ReviewableItem> = (0..30)
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
        repository = ReviewableRepositoryImpl(
            db,
            "reviewableTest",
            CourseRepositoryImpl.COURSE_CODE_FIELD_NAME
        ) { it.toCourse() }
        val c = courses.map { repository.add(it) }
        c.forEach { runTest { it.await() } }
    }

    @After
    fun teardown() {
        RepositoryUtil.clear(repository.collection)
    }

    @Test
    fun searchWorksForAFullMatch() = runTest {
        onTeacherSearch(personalizedTeacher, 1) {
            assertEquals(personalizedCourse, it.first())
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

    @Test
    fun loadMostRatedReturnsCourseWith5Reviews() = runTest {
        repository.loadMostRated(3u)
            .mapEach { it.numReviews }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(3, it.data.size)
                        it.data
                            .forEach { numReviews -> assertEquals(5, numReviews) }
                    }
                }
            }
        repository.loadMostRated(20u)
            .mapEach { it.numReviews }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        val sorted = it.data.sortedBy { n -> n }.reversed()
                        assertEquals(sorted, it.data)
                        assertEquals(23, it.data.size)
                    }
                }
            }

    }

    @Test
    fun test() = runTest {
        val query = repository.query()
            .orderBy(ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME, Query.Direction.DESCENDING)
        repository.load(query, 10u)
            .collect {
                when (it) {
                    is QueryState.Failure -> {}
                    is QueryState.Loading -> {}
                    is QueryState.Success ->
                        assertEquals(10, it.data.size)
                }
            }
    }

    @Test
    fun loadBestRatedReturnsCourseWith5Reviews() = runTest {
        repository.loadBestRated(3u)
            .mapEach { it.averageGrade }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success ->
                        it.data
                            .forEach { grade -> assertEquals(5.0, grade) }
                }
            }
        repository.loadBestRated(20u)
            .mapEach { it.averageGrade }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        val sorted = it.data.sortedBy { n -> n }.reversed()
                        assertEquals(sorted, it.data)
                        assertEquals(23, it.data.size)
                    }
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
                            min(SearchableRepository.LIMIT_QUERY_SEARCH.toInt(), numberOfMatch),
                            it.data.size
                        )
                    }
                }

            }
}