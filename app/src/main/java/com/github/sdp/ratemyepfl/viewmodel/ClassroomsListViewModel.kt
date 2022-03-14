package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.placeholder.ClassroomsRepository
import com.github.sdp.ratemyepfl.placeholder.DataSource

class ClassroomsListViewModel(classrooms : ClassroomsRepository) : ViewModel() {

    private val roomsLiveData = MutableLiveData(classrooms.get())

    fun getRooms(): LiveData<Set<Classroom>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}