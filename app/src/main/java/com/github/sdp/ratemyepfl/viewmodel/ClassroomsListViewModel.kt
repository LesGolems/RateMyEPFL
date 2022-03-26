package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomsListViewModel @Inject constructor(private val repository: ItemsRepositoryInterface) :
    ViewModel() {

    private var roomsLiveData = MutableLiveData<List<Classroom>>()

    init {
        viewModelScope.launch {
            roomsLiveData.value = repository.getClassrooms()
        }
    }

    fun getRooms(): LiveData<List<Classroom>> {
        return roomsLiveData
    }

}