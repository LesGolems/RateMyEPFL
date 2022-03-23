package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(private val repository: ItemsRepositoryInterface) :
    ViewModel() {

    private var coursesLiveData = MutableLiveData<List<Course?>>()

    init {
        viewModelScope.launch {
            coursesLiveData.value = repository.getCourses()
        }
    }

    fun getCourses(): LiveData<List<Course?>> {
        return coursesLiveData
    }
}