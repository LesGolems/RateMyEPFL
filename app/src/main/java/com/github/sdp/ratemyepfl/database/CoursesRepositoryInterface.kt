package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course

interface CoursesRepositoryInterface {

    suspend fun get(): List<Course?>
}
