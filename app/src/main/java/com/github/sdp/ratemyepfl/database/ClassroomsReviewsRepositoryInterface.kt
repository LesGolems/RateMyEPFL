package com.github.sdp.ratemyepfl.database

interface ClassroomsReviewsRepositoryInterface {
    fun add(value: ClassroomReview)
    suspend fun get(): List<ClassroomReview?>
    suspend fun getByClassroom(id: String?): List<ClassroomReview?>
}