package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.model.items.Reviewable
import kotlinx.coroutines.launch

sealed class ReviewableListViewModel<T : Reviewable>(val repository: ItemRepository<T>) :
    ViewModel() {
    private var items = MutableLiveData<List<T>>()

    init {
        viewModelScope.launch {
            items.value = repository.getItems()
        }
    }

    fun getItemsAsLiveData(): LiveData<List<T>> {
        return items
    }
}