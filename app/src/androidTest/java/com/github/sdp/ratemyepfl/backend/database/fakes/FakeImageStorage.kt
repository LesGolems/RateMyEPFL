package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.TestUtils.resourceToBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeImageStorage @Inject constructor() : Storage<ImageFile> {

    init {
        images["12345"] = ImageFile("12345", resourceToBitmap(R.raw.pp1))
    }

    companion object {
        val images = HashMap<String, ImageFile>()

        val pictureIds = listOf(
            R.raw.room3,
            R.raw.room1,
            R.raw.room4,
            R.raw.room2,
            R.raw.room5,
            R.raw.room6
        )

        val pictures = pictureIds.map {
            ImageFile(it.toString(), resourceToBitmap(it))
        }
    }

    override val MAX_ITEM_SIZE: Long
        get() = 1024 * 1024

    override fun get(id: String): Flow<ImageFile> = flow {
        if (images.containsKey(id)) {
            emit(images[id]!!)
        }
    }

    override suspend fun add(item: ImageFile) {
        images[item.id] = item
    }

    override suspend fun remove(id: String) {
        images.remove(id)
    }

    override fun getByDirectory(dir: String): Flow<ImageFile> = flow {
        pictures.forEach {
            emit(it)
        }
    }

    override suspend fun addInDirectory(item: ImageFile, dir: String) {
        images[dir] = item
    }

    override suspend fun removeInDirectory(id: String, dir: String) {
        TODO("Not yet implemented")
    }
}