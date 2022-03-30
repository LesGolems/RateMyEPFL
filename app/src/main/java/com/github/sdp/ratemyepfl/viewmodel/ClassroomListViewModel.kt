package com.github.sdp.ratemyepfl.viewmodel

import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassroomListViewModel @Inject constructor(repository: ItemRepository<Classroom>) :
    ReviewableListViewModel<Classroom>(repository)