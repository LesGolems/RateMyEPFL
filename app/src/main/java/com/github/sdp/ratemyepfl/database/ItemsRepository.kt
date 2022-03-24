package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import javax.inject.Inject

class ItemsRepository @Inject constructor() : ItemsRepositoryInterface, Repository() {
    private val collectionCourses = db.collection("courses")
    private val collectionRooms = db.collection("rooms")

    companion object {
        private const val TAG_COURSES = "CourseRepository"
        private const val TAG_ROOMS = "ClassroomRepository"
    }

    override suspend fun getCourses(): List<Course?> {
        return getLimit(collectionCourses, 50)
            .mapNotNull { obj -> obj.toCourse() }
    }

    suspend fun getByIdCourses(id: String): Course? {
        return getById(collectionCourses, id).toCourse()
    }

    override suspend fun getClassrooms(): List<Classroom?> {
        return getLimit(collectionRooms, 50)
            .mapNotNull { obj -> obj.toClassroom() }
    }

    suspend fun getByIdClassrooms(id: String): Classroom? {
        return getById(collectionRooms, id).toClassroom()
    }

}