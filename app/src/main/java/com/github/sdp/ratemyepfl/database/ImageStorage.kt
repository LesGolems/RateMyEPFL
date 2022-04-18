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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorage @Inject constructor() : Storage<ImageFile> {
    private val storageRef = FirebaseStorage.getInstance().reference.child("images")

    override val MAX_ITEM_SIZE: Long = 1024 * 1024 // 1 MB

    companion object {
        const val FILE_EXTENSION: String = ".jpg" // JPEG images
    }

    override suspend fun get(id: String): ImageFile? {
        val imageRef = storageRef.child("$id$FILE_EXTENSION")
        return getImage(imageRef)
    }

    override suspend fun add(item: ImageFile) {
        addImage(item, item.id)
    }

    override suspend fun remove(item: ImageFile) {
        storageRef
            .child("${item.id}$FILE_EXTENSION")
            .delete()
            .addOnSuccessListener {
                Log.i("ImageStorage: ", "Successfully deleted item ${item.id}")
            }
            .addOnFailureListener {
                Log.e("ImageStorage: ", "Failed to delete item ${item.id}", it.cause)
            }
            .await()
    }

    override suspend fun getByDirectory(dir: String): List<ImageFile> {
        return storageRef
            .child(dir)
            .listAll()
            .await()
            .items
            .mapNotNull { getImage(it) }
    }

    override suspend fun addInDirectory(item: ImageFile, dir: String) {
        addImage(item, "$dir/${item.id}")
    }

    /**
     * Returns the [ImageFile] at the reference [imageRef].
     */
    private suspend fun getImage(imageRef: StorageReference): ImageFile? {
        return try {
            val id = imageRef.name.dropLast(FILE_EXTENSION.length)

            val byteArray = imageRef
                .getBytes(MAX_ITEM_SIZE)
                .addOnSuccessListener {
                    Log.i("ImageStorage: ", "Successfully read item $id")
                }
                .addOnFailureListener {
                    Log.e("ImageStorage: ", "Failed to read item $id", it.cause)
                }
                .await()

            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ImageFile(id, bitmap)
        } catch (e: StorageException) {
            null
        }
    }

    /**
     * Adds [item] to the collection at location [path].
     */
    private suspend fun addImage(item: ImageFile, path: String) {
        if (item.size > MAX_ITEM_SIZE) {
            throw IllegalArgumentException("Image size should be less than $MAX_ITEM_SIZE bytes.")
        }

        val stream = ByteArrayOutputStream()
        item.data.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        storageRef
            .child("$path$FILE_EXTENSION")
            .putBytes(stream.toByteArray())
            .addOnSuccessListener {
                Log.i("ImageStorage: ", "Successfully added item ${item.id}")
            }
            .addOnFailureListener {
                Log.e("ImageStorage: ", "Failed to add item ${item.id}", it.cause)
            }
            .await()
    }
}