package com.github.sdp.ratemyepfl.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.ratemyepfl.database.CourseReviewDatabase

class CourseReviewDatabaseViewModel(db: CourseReviewDatabase) : ViewModel() {

    private val database: LiveData<CourseReviewDatabase> = MutableLiveData(db)

    fun getDatabase() = database.value

    class Factory(val db: CourseReviewDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseReviewDatabaseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CourseReviewDatabaseViewModel(
                    this.db
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}