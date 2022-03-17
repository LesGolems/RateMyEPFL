package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.placeholder.ClassroomsRepository
import com.github.sdp.ratemyepfl.placeholder.DataSource

class ClassroomsListViewModel(classrooms : ClassroomsRepository) : ViewModel() {

    //private val roomsLiveData: LiveData<Classroom?> = classrooms.getAll().asLiveData()
    private val roomsLiveData = MutableLiveData<Set<Classroom>>()

    /*fun updateRooms() {
        runBlocking {
            launch {
                roomsLiveData.value = classrooms.get()
            }
        }
    }*/


    fun getRooms(): LiveData<Set<Classroom>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}