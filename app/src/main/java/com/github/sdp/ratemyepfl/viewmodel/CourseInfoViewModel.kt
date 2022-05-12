package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseInfoViewModel @Inject constructor(
    private val courseRepo: CourseRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ReviewableInfoViewModel(gradeInfoRepo, savedStateHandle) {

    val course = MutableLiveData<Course>()

    init {
        refresh()
    }

    fun updateCourse() {
        viewModelScope.launch {
            course.postValue(courseRepo.getCourseById(id))
        }
    }

    fun refresh() {
        updateCourse()
        refreshGrade()
    }
}