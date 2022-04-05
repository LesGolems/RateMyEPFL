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

    /**
     * The ImageStorage follows the singleton design pattern.
     */
    companion object {
        val instance = ImageStorage()
    }

    private fun storageReference() : StorageReference {
        return FirebaseStorage.getInstance()
            .reference
            .child("images")
    }

    // Maximum supported size for an image by Firebase Storage
    override val MAX_ITEM_SIZE: Long = 1024*1024

    /**
     * Returns the ImageFile for the given [id].
     * Returns null in case of errors.
     */
    override suspend fun get(id: String): ImageFile? {
        return try {
            val ba = storageReference()
                .child("$id.jpg")
                .getBytes(MAX_ITEM_SIZE)
                .addOnSuccessListener {
                    Log.i("ImageStorage: ", "Successfully read item $id")
                }
                .addOnFailureListener{
                    Log.e("ImageStorage: ", "Failed to read item $id", it.cause)
                }
                .await()

            val bitmap = BitmapFactory.decodeByteArray(ba, 0, ba.size)
            ImageFile(id, bitmap)

        } catch (e: StorageException) {
            null
        }
    }

    /**
     * Adds an [item] to the collection.
     * Throws an IllegalArgumentException if the item is too big for the collection.
     */
    override suspend fun put(item: ImageFile) {
        if (item.size > MAX_ITEM_SIZE) {
            throw IllegalArgumentException("Image size should be less than $MAX_ITEM_SIZE bytes.")
        }

        val baos = ByteArrayOutputStream()
        item.data.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        storageReference().child(item.id + ".jpg")
            .putBytes(baos.toByteArray())
            .addOnSuccessListener {
                Log.i("ImageStorage: ", "Successfully added item ${item.id}")
            }
            .addOnFailureListener{
                Log.e("ImageStorage: ", "Failed to add item ${item.id}", it.cause)
            }
            .await()
    }

    /**
     * Removes an [item] from the collection.
     */
    override suspend fun remove(item: ImageFile) {
        storageReference()
            .child(item.id + ".jpg")
            .delete()
            .addOnSuccessListener {
                Log.i("ImageStorage: ", "Successfully deleted item ${item.id}")
            }
            .addOnFailureListener{
                Log.e("ImageStorage: ", "Failed to delete item ${item.id}", it.cause)
            }
            .await()
    }
}