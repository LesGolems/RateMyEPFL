package com.github.sdp.ratemyepfl.viewmodel.filter

import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.query.OrderDirection
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Course

sealed interface CourseFilter : ReviewableFilter<Course> {

    override fun conflictWith(filter: ReviewableFilter<Course>): Boolean =
        this == filter || when (this) {
            is AlphabeticalOrder -> filter is AlphabeticalOrderReversed
            is AlphabeticalOrderReversed -> filter is AlphabeticalOrder
            BestRated -> filter is WorstRated
            is Credits -> false
            is Section -> false
            is Cycle -> false
            WorstRated -> filter is BestRated
        }


    object AlphabeticalOrder : CourseFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(CourseRepositoryImpl.TITLE_FIELD_NAME)
    }

    object AlphabeticalOrderReversed : CourseFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(CourseRepositoryImpl.TITLE_FIELD_NAME, OrderDirection.DESCENDING)
    }

    data class Credits(val number: Int) : CourseFilter {
        init {
            if (number <= 0) {
                throw IllegalArgumentException("A course cannot have a negative number of credits")
            }
        }

        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME)
            .whereEqualTo(CourseRepositoryImpl.CREDITS_FIELD_NAME, number)

    }

    data class Section(val name: String) : CourseFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME)
            .whereEqualTo(CourseRepositoryImpl.SECTION_FIELD_NAME, name)

    }

    data class Cycle(val cycleName: String) : CourseFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery = initialQuery
            .orderBy(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME)
            .whereEqualTo(CourseRepositoryImpl.CYCLE_FIELD_NAME, cycleName)

    }

    object BestRated : CourseFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery =
            initialQuery.orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.DESCENDING)
                .orderBy(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME)
    }

    object WorstRated : CourseFilter {
        override fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery =
            initialQuery.orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.ASCENDING)
                .orderBy(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME)
    }
}