package com.github.sdp.ratemyepfl.viewmodel.filter

import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.query.OrderedQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant

sealed interface RestaurantFilter : ReviewableFilter<Restaurant> {

    override infix fun conflictWith(filter: ReviewableFilter<Restaurant>): Boolean =
        this == filter || when (this) {
            is AlphabeticalOrder -> filter is AlphabeticalOrderReversed
            is AlphabeticalOrderReversed -> filter is AlphabeticalOrder
            BestRated -> filter is WorstRated
            is Closest -> false
            WorstRated -> filter is BestRated
        }

    object AlphabeticalOrder : RestaurantFilter {
        override fun toQuery(initialQuery: Query): OrderedQuery = initialQuery
            .orderBy(RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME)

    }

    object AlphabeticalOrderReversed : RestaurantFilter {
        override fun toQuery(initialQuery: Query): OrderedQuery = initialQuery
            .orderBy(
                RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME,
                OrderDirection.DESCENDING
            )
    }

    data class Closest(val distance: Double) : RestaurantFilter {
        override fun toQuery(initialQuery: Query): OrderedQuery =
            TODO("To be implemented using a GeoHash")
    }

    object BestRated : RestaurantFilter {
        override fun toQuery(initialQuery: Query): OrderedQuery = initialQuery
            .orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.DESCENDING)
                .orderBy(RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME)
    }

    object WorstRated : RestaurantFilter {
        override fun toQuery(initialQuery: Query): OrderedQuery =
            initialQuery.orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.ASCENDING)
                .orderBy(RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME)
    }
}
