package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Classroom
import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomsListViewModel @Inject constructor(private val repository: ItemsRepositoryInterface) : ViewModel() {

    private var roomsLiveData = MutableLiveData<List<Classroom?>>()

    init {
        viewModelScope.launch{
            roomsLiveData.value = repository.getClassrooms()
        }
    }

    fun getRooms(): LiveData<List<Classroom?>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}