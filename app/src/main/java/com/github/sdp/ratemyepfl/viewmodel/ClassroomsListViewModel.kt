package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.placeholder.ClassroomsRepository
import com.github.sdp.ratemyepfl.placeholder.DataSource

class ClassroomsListViewModel(classrooms : ClassroomsRepository) : ViewModel() {

    private val roomsLiveData: LiveData<Classroom?> = classrooms.getAll().asLiveData()

    fun getRooms(): LiveData<Classroom?> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}