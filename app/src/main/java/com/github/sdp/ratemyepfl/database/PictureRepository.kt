package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.R
import javax.inject.Inject

class PictureRepository @Inject constructor() : PictureRepositoryInterface {

    companion object {
        // Fake photo ids
        private val FAKE_PHOTOS = listOf(
            R.raw.room3,
            R.raw.room1,
            R.raw.room4,
            R.raw.room2,
            R.raw.room5,
            R.raw.room6
        )
    }

    // Not linked to Firebase Storage yet
    override suspend fun getPhotosByClassroomId(id: String?): List<Int> {
        return FAKE_PHOTOS
    }
}