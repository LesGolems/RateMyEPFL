package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ClassroomRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomListViewModel @Inject constructor(repository: ClassroomRepositoryInterface) :
    ViewModel() {

    val classrooms = MutableLiveData<List<Classroom>>()

    init {
        viewModelScope.launch {
            classrooms.value = repository.getClassrooms()
        }
    }
}
