package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.placeholder.ClassroomsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ClassroomsListViewModel(val classrooms: ClassroomsRepository): ViewModel() {

    private val roomsLiveData = MutableLiveData<Set<Classroom>>()

    fun updateRooms() {
        runBlocking {
            launch {
                roomsLiveData.value = classrooms.get()
            }
        }
    }

    fun getRooms(): LiveData<Set<Classroom>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}