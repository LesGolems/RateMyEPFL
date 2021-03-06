package com.github.sdp.ratemyepfl.viewmodel.main

import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.viewmodel.filter.ClassroomFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassroomListViewModel @Inject constructor(repository: ClassroomRepositoryImpl) :
    ReviewableListViewModel<Classroom>(
        repository,
        ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME,
        ClassroomFilter.BestRated
    )
