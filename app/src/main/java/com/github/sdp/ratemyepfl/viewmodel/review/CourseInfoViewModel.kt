package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseInfoViewModel @Inject constructor(
    private val courseRepo: CourseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    val course = MutableLiveData<Course>()

    init {
        updateCourse()
    }

    fun updateCourse() {
        viewModelScope.launch {
            course.postValue(courseRepo.getCourseByCourseCode(id))
        }
    }

}