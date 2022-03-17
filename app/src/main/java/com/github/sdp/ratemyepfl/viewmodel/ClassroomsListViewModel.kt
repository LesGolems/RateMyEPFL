package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.placeholder.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassroomsListViewModel @Inject constructor(dataSource: DataSource) : ViewModel() {
    private val roomsLiveData = dataSource.getRoomList()

    fun getRooms(): LiveData<List<Classroom>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}