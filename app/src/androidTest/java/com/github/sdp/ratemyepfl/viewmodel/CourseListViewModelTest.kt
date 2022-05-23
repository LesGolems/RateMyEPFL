package com.github.sdp.ratemyepfl.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.filter.CourseFilter
import com.github.sdp.ratemyepfl.viewmodel.main.CourseListViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
class CourseListViewModelTest {

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: CourseRepositoryImpl

    private val fake = "fake"
    private val personalizedTeacher = "myPersonalTeacher"
    private val courseBuilder = Course(
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
        .copy(teacher = personalizedTeacher, title = title, courseCode = courseCode)

    private val courses: List<Course> = (0..30)
        .map { n ->
            Course(
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
            ).copy(courseCode = n.toString(), grade = ((n + 2) % 5 + 1).toDouble())
        }.plus(personalizedCourse)

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        courses.forEach {
            runTest { repository.add(it).await() }
        }
    }

    @After
    fun teardown() {
        courses.forEach {
            runTest { repository.remove(it.getId()).await() }
        }
    }

    @Test
    fun loadAddTheLoadedItemsInTheList() = runTest {
        val viewModel = CourseListViewModel(repository)
        viewModel.load(CourseFilter.AlphabeticalOrder, 1u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(it.data, viewModel.elements.value)
                    }
                }
            }
    }

    @Test
    fun loadMoreAddTheLoadedItemsInTheList() = runTest {
        val viewModel = CourseListViewModel(repository)
        var contained = listOf<Course>()
        viewModel.load(CourseFilter.AlphabeticalOrder, 1u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        contained = it.data
                    }
                }
            }
        viewModel.loadMore(1u).collect {
            when (it) {
                is QueryState.Failure -> throw it.error
                is QueryState.Loading -> {}
                is QueryState.Success -> {
                    assertEquals(2, viewModel.elements.value?.size)
                    assertEquals(true, viewModel.elements.value?.containsAll(contained))
                }
            }
        }

    }

    @Test
    fun loadIfAbsentLoadsIfNothingWasLoadBefore() = runTest {
        val viewModel = CourseListViewModel(repository)
        viewModel.loadIfAbsent(CourseFilter.AlphabeticalOrder, 1u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(1, viewModel.elements.value?.size)
                    }
                }
            }
    }

    @Test
    fun loadIfAbsentLoadsRetrieveTheLoadedValue() = runTest {
        val viewModel = CourseListViewModel(repository)
        val filter = CourseFilter.AlphabeticalOrder
        viewModel.load(filter, 10u).collect()
        viewModel.loadIfAbsent(filter, 1u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> throw Exception("Should not load anything")
                    is QueryState.Success -> {
                        assertEquals(10, viewModel.elements.value?.size)
                    }
                }
            }
    }


    @Test
    fun searchCorrectlyUpdatesTheList() = runTest {
        val viewModel = CourseListViewModel(repository)
        viewModel.search(title)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw it.error
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(
                            true,
                            viewModel.elements.value?.all { course -> course.title == title })
                    }
                }
            }
    }
}