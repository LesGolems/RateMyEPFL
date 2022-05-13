package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course

class AddClassViewModel : ViewModel() {

    val course: MutableLiveData<Course?> = MutableLiveData(null)
    val room: MutableLiveData<Classroom?> = MutableLiveData(null)
    val day: MutableLiveData<Int?> = MutableLiveData(null)
}