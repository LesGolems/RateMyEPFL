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
class ClassroomListViewModel @Inject constructor(repository: ClassroomRepository) :
    ReviewableListViewModel<Classroom>(
        repository,
        ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME,
        ClassroomFilter.BestRated
    ) {

    val classrooms: MutableLiveData<List<Classroom>> = elements


    companion object {
        private fun alphabeticalOrderQuery(repository: ClassroomRepository) = repository
            .query()
            .orderBy(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME)

        private fun alphabeticalOrderReversedQuery(repository: ClassroomRepository) = repository
            .query()
            .orderBy(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME, OrderDirection.DESCENDING)

        private fun bestRatedQuery(repository: ClassroomRepository) = repository
            .query()
            .orderBy(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME, OrderDirection.DESCENDING)
    }

}
