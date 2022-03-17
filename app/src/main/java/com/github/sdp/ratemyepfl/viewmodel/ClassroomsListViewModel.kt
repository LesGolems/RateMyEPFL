package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.database.ClassroomsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomsListViewModel @Inject constructor(private val repository: ClassroomsRepositoryInterface) : ViewModel() {

    private var roomsLiveData = MutableLiveData<List<Classroom?>>()

    init {
        viewModelScope.launch{
            roomsLiveData.value = repository.get()
        }
    }


    fun getRooms(): LiveData<List<Classroom?>> {
        return roomsLiveData
    }

    private fun loadRooms() {
        // Do an asynchronous operation to fetch classrooms.
    }
}