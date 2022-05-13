package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.viewmodel.filter.ClassroomFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassroomListViewModel @Inject constructor(repository: ClassroomRepositoryImpl) :
    ReviewableListViewModel<Classroom>(
        repository,
        ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME,
        ClassroomFilter.BestRated
    ) {

}
