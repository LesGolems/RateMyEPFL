package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomListViewModel @Inject constructor(repository: ClassroomRepository) :
    ViewModel() {

    val classrooms = MutableLiveData<List<Classroom>>()

    init {
        viewModelScope.launch {
            classrooms.value = repository.getClassrooms()
        }
    }
}
