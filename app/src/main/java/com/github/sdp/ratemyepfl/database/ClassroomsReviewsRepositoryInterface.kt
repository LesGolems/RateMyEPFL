package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.ClassroomReview

interface ClassroomsReviewsRepositoryInterface {
    suspend fun get(): List<ClassroomReview?>
    suspend fun getByClassroom(id: String?): List<ClassroomReview?>
}