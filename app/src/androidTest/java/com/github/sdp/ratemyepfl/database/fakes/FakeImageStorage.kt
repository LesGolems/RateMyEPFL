package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.TestUtils.drawableToBitmap
import javax.inject.Inject

class FakeImageStorage @Inject constructor() : Storage<ImageFile> {
    private val images = HashMap<String, ImageFile>()

    companion object {
        val pictureIds = listOf(
            R.drawable.room3,
            R.drawable.room1,
            R.drawable.room4,
            R.drawable.room2,
            R.drawable.room5,
            R.drawable.room6
        )

        val pictures = pictureIds.map {
            ImageFile(it.toString(), drawableToBitmap(it))
        }
    }

    override val MAX_ITEM_SIZE: Long
        get() = 1024 * 1024

    override suspend fun get(id: String): ImageFile? {
        if (images.containsKey(id)) {
            return images[id]
        }
        return null
    }

    override suspend fun add(item: ImageFile) {
        images[item.id] = item
    }

    override suspend fun remove(item: ImageFile) {
        images.remove(item.id)
    }

    override suspend fun getByDirectory(dir: String): List<ImageFile> {
        return pictures
    }

    override suspend fun addInDirectory(item: ImageFile, dir: String) {
        TODO("Will be implemented in my second Sprint task")
    }
}