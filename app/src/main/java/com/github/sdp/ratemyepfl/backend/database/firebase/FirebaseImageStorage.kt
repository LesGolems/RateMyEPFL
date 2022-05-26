package com.github.sdp.ratemyepfl.backend.database.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseImageStorage @Inject constructor(storage: FirebaseStorage) : Storage<ImageFile> {
    private val storageRef = storage.reference.child("images")

    override val MAX_ITEM_SIZE: Long = 20 * 1024 * 1024 // 1 MB

    companion object {
        const val FILE_EXTENSION: String = ".jpg" // JPEG images
    }

    override fun get(id: String): Flow<ImageFile> = flow {
        val imageRef = storageRef.child("$id$FILE_EXTENSION")
        emit(getImage(imageRef))
    }

    override suspend fun add(item: ImageFile) {
        addImage(item, item.id)
    }

    override suspend fun remove(id: String) {
        storageRef
            .child("${id}$FILE_EXTENSION")
            .delete()
            .await()
    }

    override fun getByDirectory(dir: String): Flow<ImageFile> = flow {
        val references = storageRef
            .child(dir)
            .listAll()
            .await()
            .items
            .filterNotNull()

        for (reference in references) {
            emit(getImage(reference))
        }
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
    private suspend fun getImage(imageRef: StorageReference): ImageFile {
        val id = imageRef.name.dropLast(FILE_EXTENSION.length)

        val byteArray = imageRef
            .getBytes(MAX_ITEM_SIZE)
            .await()

        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return ImageFile(id, bitmap)
    }

    /**
     * Adds [item] to the collection at location [path].
     */
    private suspend fun addImage(item: ImageFile, path: String) {
        val stream = ByteArrayOutputStream()
        item.data.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        storageRef
            .child("$path$FILE_EXTENSION")
            .putBytes(stream.toByteArray())
            .await()
    }
}