package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ItemsRepository @Inject constructor() : ItemsRepositoryInterface, Repository() {
    private val collectionCourses = db.collection("courses")
    private val collectionRooms = db.collection("rooms")

    companion object {
        private const val TAG_COURSES = "CourseRepository"
        private const val TAG_ROOMS = "ClassroomRepository"
    }

    override suspend fun getCourses(): List<Course?> {
        return collectionCourses.limit(50).get().await().mapNotNull { obj -> obj.toCourse() }
    }

    suspend fun getByIdCourses(id: String): Course? {
        return collectionCourses.document(id).get().await().toCourse()
    }

    override suspend fun getClassrooms(): List<Classroom?> {
        return collectionRooms.limit(50).get().await().mapNotNull { obj -> obj.toClassroom() }
    }

    suspend fun getByIdClassrooms(id: String): Classroom? {
        return collectionRooms.document(id).get().await().toClassroom()
    }

}