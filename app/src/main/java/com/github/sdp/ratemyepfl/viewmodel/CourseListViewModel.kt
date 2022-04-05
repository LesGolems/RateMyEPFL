package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.CourseRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(repository: CourseRepositoryInterface) : ViewModel() {

    private var items = MutableLiveData<List<Course>>()

    init {
        viewModelScope.launch {
            items.value = repository.getCourses()
        }
    }

    fun getItemsAsLiveData(): LiveData<List<Course>> {
        return items
    }
}