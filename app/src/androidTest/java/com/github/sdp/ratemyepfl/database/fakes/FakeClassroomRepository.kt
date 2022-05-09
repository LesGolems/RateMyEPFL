package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor() : ClassroomRepository {

    companion object {
        val CLASSROOM_LIST = listOf(
            Classroom(
                name = "CM3"
            ),
            Classroom(
                name = "CE-1515"
            ),
            Classroom(
                name = "AAC 2 31"
            ),
            Classroom(
                name = "ELA 2"
            )
        )


        var roomById = Classroom(name = "CM3")
    }


    override suspend fun getClassrooms(): List<Classroom> = CLASSROOM_LIST

    override suspend fun getRoomById(id: String): Classroom = roomById

    override fun search(prefix: String): QueryResult<List<Classroom>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}