package com.github.sdp.ratemyepfl.viewmodel.filter

import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.backend.database.query.OrderDirection
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Classroom

sealed interface ClassroomFilter : ReviewableFilter<Classroom> {

    override fun conflictWith(filter: ReviewableFilter<Classroom>): Boolean =
        this == filter || when (this) {
            is AlphabeticalOrder -> filter is AlphabeticalOrderReversed
            is AlphabeticalOrderReversed -> filter is AlphabeticalOrder
            is BestRated -> filter is WorstRated
            is WorstRated -> filter is BestRated
        }

    object AlphabeticalOrder : ClassroomFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME)
    }

    object AlphabeticalOrderReversed : ClassroomFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(
                ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME,
                OrderDirection.DESCENDING
            )
    }

    object BestRated : ClassroomFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.DESCENDING)
            .orderBy(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME)
    }

    object WorstRated : ClassroomFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery =
            initialQuery.orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.ASCENDING)
                .orderBy(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME)
    }

}