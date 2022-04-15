package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.PictureRepositoryInterface
import javax.inject.Inject

class FakePictureRepository @Inject constructor() : PictureRepositoryInterface {

    companion object {
        val PHOTO_LIST = listOf(
            R.raw.room3,
            R.raw.room1,
            R.raw.room4,
            R.raw.room2,
            R.raw.room5,
            R.raw.room6
        )
    }

    override suspend fun getPhotosByClassroomId(id: String?): List<Int> {
        return PHOTO_LIST
    }
}