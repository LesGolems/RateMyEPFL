package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.ClassroomReview

interface ClassroomsReviewsRepositoryInterface {
    fun add(value: ClassroomReview)
    suspend fun get(): List<ClassroomReview?>
    suspend fun getByClassroom(id: String?): List<ClassroomReview?>
}