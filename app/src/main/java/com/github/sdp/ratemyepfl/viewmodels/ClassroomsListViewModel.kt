package com.github.sdp.ratemyepfl.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.Classroom
import com.github.sdp.ratemyepfl.placeholder.DataSource

class ClassroomsListViewModel(val dataSource: DataSource = DataSource()) : ViewModel() {

    val roomsLiveData = dataSource.getRoomList()

    fun getRooms(): LiveData<List<Classroom>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}