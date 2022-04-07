package com.github.sdp.ratemyepfl.database

interface PictureRepositoryInterface {
    suspend fun getPhotosByClassroomId(id: String?): List<Int>
}