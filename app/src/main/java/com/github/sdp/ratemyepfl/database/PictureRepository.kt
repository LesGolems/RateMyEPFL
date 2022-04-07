package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.R
import javax.inject.Inject

class PictureRepository @Inject constructor() : PictureRepositoryInterface {

    companion object {
        // Fake photo ids
        private val FAKE_PHOTOS = listOf(
            R.drawable.room3,
            R.drawable.room1,
            R.drawable.room4,
            R.drawable.room2,
            R.drawable.room5,
            R.drawable.room6
        )
    }

    // Not linked to Firebase Storage yet
    override suspend fun getPhotosByClassroomId(id: String?): List<Int> {
        return FAKE_PHOTOS
    }
}