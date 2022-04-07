package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import javax.inject.Inject

class FakeImageStorage @Inject constructor() : Storage<ImageFile> {
    private val images = HashMap<String, ImageFile>()
    override val MAX_ITEM_SIZE: Long
        get() = 1024*1024

    override suspend fun get(id: String): ImageFile? {
        if (images.containsKey(id)) {
            return images.get(id)
        }
        return null
    }

    override suspend fun put(item: ImageFile) {
        images.put(item.id, item)
    }

    override suspend fun remove(item: ImageFile) {
        images.remove(item.id)
    }
}