package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.PictureRepositoryInterface
import javax.inject.Inject

class FakePictureRepository @Inject constructor() : PictureRepositoryInterface {

    companion object {
        val PHOTO_LIST = listOf(
            R.drawable.room3,
            R.drawable.room1,
            R.drawable.room4,
            R.drawable.room2,
            R.drawable.room5,
            R.drawable.room6
        )
    }

    override suspend fun getPhotosByClassroomId(id: String?): List<Int> {
        return PHOTO_LIST
    }
}