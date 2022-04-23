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
class ImageStorage @Inject constructor(storage: FirebaseStorage) : Storage<ImageFile> {
    private val storageRef = storage.reference.child("images")

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

    override suspend fun remove(path: String) {
        storageRef
            .child("${path}$FILE_EXTENSION")
            .delete()
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

    override suspend fun removeInDirectory(id: String, dir: String) {
        remove("$dir/${id}")
    }

    /**
     * Returns the [ImageFile] at the reference [imageRef].
     */
    private suspend fun getImage(imageRef: StorageReference): ImageFile? {
        return try {
            val id = imageRef.name.dropLast(FILE_EXTENSION.length)

            val byteArray = imageRef
                .getBytes(MAX_ITEM_SIZE)
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
            .await()
    }
}