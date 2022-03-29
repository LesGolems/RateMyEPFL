package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassroomsListViewModel @Inject constructor(repository: ItemRepository<Classroom>) : ReviewableListViewModel<Classroom>(repository){

    fun getRooms(): LiveData<List<Classroom>> {
        return getItemsAsLiveData()
    }

}