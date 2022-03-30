package com.github.sdp.ratemyepfl.viewmodel

import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(repository: ItemRepository<Course>) :
    ReviewableListViewModel<Course>(repository)