package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.github.sdp.ratemyepfl.items.Course
import org.junit.Assert.*
import org.junit.Test

class CourseReviewViewModelTest {

    @Test
    fun factoryCreatesAViewModelWithTheCorrectCourse() {
        val course = Course("Sweng", "CS", "Candea", 4, "CS-306")
        val model = CourseReviewViewModel(course)
        assertEquals(course, model.course)
    }


}