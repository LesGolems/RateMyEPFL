package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CourseRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var repository: ItemRepository<Course>

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getItemsReturnsCorrectItems() = runTest {
        this.launch {
            repository.getItems()
                .zip(FakeCourseRepository.COURSE_LIST)
                .forEach { (i1, i2) ->
                    assertEquals(i2, i1)
                }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getItemByIdReturnsCorrectItem() = runTest {
        this.launch {
            assertEquals(FakeCourseRepository.COURSE_BY_ID, repository.getItemById("fake id"))
        }
    }

}