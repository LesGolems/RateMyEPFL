package com.github.sdp.ratemyepfl.fragment.review

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.RoomPictureAdapter
import com.github.sdp.ratemyepfl.database.ImageStorage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.ImageUtils
import com.github.sdp.ratemyepfl.viewmodel.ClassroomPictureViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class RoomReviewPictureFragment : Fragment(R.layout.fragment_room_review_picture) {

    companion object {
        /* Number of columns in the image grid */
        private const val NUM_COLUMNS = 2
        private const val CAPTURE_PHOTO = 1
        private const val SELECT_PHOTO = 2
    }

    private lateinit var pictureAdapter: RoomPictureAdapter
    private lateinit var pictureRecyclerView: RecyclerView

    private lateinit var capturePhotoFAB: FloatingActionButton
    private lateinit var selectPhotoFAB: FloatingActionButton

    private lateinit var currentPhotoPath: String

    private val pictureViewModel by activityViewModels<ClassroomPictureViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pictureRecyclerView = view.findViewById(R.id.pictureRecyclerView)
        val gridLayoutManager =
            StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
        pictureRecyclerView.layoutManager = gridLayoutManager

        pictureAdapter = RoomPictureAdapter()
        pictureRecyclerView.adapter = pictureAdapter

        pictureViewModel.pictures.observe(viewLifecycleOwner) {
            pictureAdapter.setData(it.toMutableList())
        }

        selectPhotoFAB = view.findViewById(R.id.selectPhotoFAB)
        selectPhotoFAB.setOnClickListener { startGallery() }

        capturePhotoFAB = view.findViewById(R.id.capturePhotoFAB)
        capturePhotoFAB.setOnClickListener { startCamera() }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_PHOTO -> {
                if (resultCode == RESULT_OK && data != null) {
                    val photoUri = data.data
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver, photoUri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    uploadPicture(bitmap)
                }
            }
            CAPTURE_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    uploadPicture(bitmap)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pictureViewModel.updatePicturesList()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), SELECT_PHOTO)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        val photoUri = FileProvider.getUriForFile(
            requireContext(), "com.github.sdp.ratemyepfl.fileprovider", photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, CAPTURE_PHOTO)
    }

    private fun uploadPicture(bitmap: Bitmap) {
        val id = ImageUtils.timeStamp()
        try {
            pictureViewModel.uploadPicture(ImageFile(id, bitmap))
            Toast.makeText(context, "Your photo was uploaded successfully!", Toast.LENGTH_LONG)
                .show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun createImageFile(): File {
        val id = ImageUtils.timeStamp()
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(id, ImageStorage.FILE_EXTENSION, storageDir)
            .apply { currentPhotoPath = absolutePath }
    }
}