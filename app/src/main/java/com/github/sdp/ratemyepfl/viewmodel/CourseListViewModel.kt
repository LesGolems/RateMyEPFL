package com.github.sdp.ratemyepfl.viewmodel

import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl.Companion.COURSE_CODE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl.Companion.TITLE_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.filter.CourseFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(private val repository: CourseRepositoryImpl) :
    ReviewableListViewModel<Course>(
        repository,
        COURSE_CODE_FIELD_NAME,
        CourseFilter.BestRated,
    ) {

    override fun search(prefix: String, number: UInt): QueryResult<List<Course>> {
        val byId = repository
            .search(COURSE_CODE_FIELD_NAME, prefix)

        val byTitle = repository
            .search(TITLE_FIELD_NAME, prefix)
        // Merge the two flows and map the content to Course
        return postResult(byId.zip(byTitle) { x, y ->
            x.flatMap { ids -> y.map { titles -> ids.plus(titles) } }
        }.asQueryResult())
    }

}