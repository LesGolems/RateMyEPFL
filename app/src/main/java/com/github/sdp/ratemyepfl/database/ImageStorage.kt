package com.github.sdp.ratemyepfl.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.github.sdp.ratemyepfl.model.ImageFile
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ImageStorage private constructor() : Storage<ImageFile> {

    companion object {
        val instance = ImageStorage()
        const val MAX_IMAGE_SIZE: Long = 1024*1024
    }

    private fun storageReference() : StorageReference {
        return FirebaseStorage.getInstance()
            .reference
            .child("images")
    }

    override val MAX_ITEM_SIZE: Long
        get() = MAX_IMAGE_SIZE

    override suspend fun get(id: String): ImageFile? {
        return try {
            val ba = storageReference()
                .child("$id.jpg")
                .getBytes(MAX_IMAGE_SIZE)
                .await()

            val bitmap = BitmapFactory.decodeByteArray(ba, 0, ba.size)
            ImageFile(id, bitmap)
        } catch (e: StorageException) {
            null
        }
    }

    override suspend fun put(item: ImageFile) {
        if (item.size > MAX_IMAGE_SIZE) {
            throw Exception("Image size should be less than $MAX_IMAGE_SIZE bytes.")
        }

        val baos = ByteArrayOutputStream()
        item.data.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        storageReference().child(item.id + ".jpg")
            .putBytes(baos.toByteArray())
            .await()
    }

    override suspend fun remove(item: ImageFile) {
        storageReference().child(item.id + ".jpg").delete()
    }
}