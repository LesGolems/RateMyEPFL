package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.TestUtils.drawableToBitmap
import javax.inject.Inject

class FakeImageStorage @Inject constructor() : Storage<ImageFile> {
  
    init {
        images.put("12345", ImageFile("12345", drawableToBitmap(R.raw.pp1)))
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

    override suspend fun remove(id: String) {
        images.remove(id)
    }

    override suspend fun getByDirectory(dir: String): List<ImageFile> {
        return pictures
    }

    override suspend fun addInDirectory(item: ImageFile, dir: String) {
        TODO("Will be implemented in my second Sprint task")
    }

    override suspend fun removeInDirectory(id: String, dir: String) {
        TODO("Not yet implemented")
    }
}