package com.github.sdp.ratemyepfl.ui.fragment.review

import android.Manifest
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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.firebase.FirebaseImageStorage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.ui.adapter.RoomPictureAdapter
import com.github.sdp.ratemyepfl.utils.FragmentUtils
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnToast
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.utils.TimeUtils
import com.github.sdp.ratemyepfl.viewmodel.review.ClassroomPictureViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class RoomReviewPictureFragment : Fragment(R.layout.fragment_room_review_picture) {

    companion object {
        /* Number of columns in the image grid */
        private const val NUM_COLUMNS = 2
        private const val CAPTURE_PHOTO_REQUEST_CODE = 1
        private const val SELECT_PHOTO_REQUEST_CODE = 2
    }

    private lateinit var pictureAdapter: RoomPictureAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout
    private lateinit var noPictureText: TextView

    private lateinit var selectPhotoFAB: FloatingActionButton
    private lateinit var capturePhotoFAB: FloatingActionButton
    private lateinit var currentPhotoPath: String

    private val viewModel by activityViewModels<ClassroomPictureViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePictureGrid(view)
        initializeGallery(view)
        initializeCamera(view)
    }

    private fun initializePictureGrid(view: View) {
        recyclerView = view.findViewById(R.id.pictureRecyclerView)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)

        swipeRefresher = view.findViewById(R.id.pictureSwipeRefresh)
        swipeRefresher.setOnRefreshListener {
            viewModel.updatePicturesList()
            swipeRefresher.isRefreshing = false
        }

        pictureAdapter = RoomPictureAdapter()
        recyclerView.adapter = pictureAdapter

        viewModel.pictures.observe(viewLifecycleOwner) {
            pictureAdapter.setData(it.toMutableList())
        }

        noPictureText = view.findViewById(R.id.noPictureText)
        viewModel.isEmpty.observe(viewLifecycleOwner) {
            FragmentUtils.emptyList(it, recyclerView, noPictureText)
        }
    }

    private fun initializeGallery(view: View) {
        val storagePermissionLauncher =
            PermissionUtils.requestPermissionLauncher({ startGallery() }, this, requireContext())

        selectPhotoFAB = view.findViewById(R.id.selectPhotoFAB)
        selectPhotoFAB.setOnClickListener {
            PermissionUtils.verifyPermissionAndExecute(
                { startGallery() },
                storagePermissionLauncher,
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun initializeCamera(view: View) {
        val cameraPermissionLauncher =
            PermissionUtils.requestPermissionLauncher({ startCamera() }, this, requireContext())

        capturePhotoFAB = view.findViewById(R.id.capturePhotoFAB)
        capturePhotoFAB.setOnClickListener {
            PermissionUtils.verifyPermissionAndExecute(
                { startCamera() },
                cameraPermissionLauncher,
                requireContext(),
                Manifest.permission.CAMERA
            )
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_PHOTO_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val photoUri = data.data
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver, photoUri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    uploadPicture(bitmap)
                }
            }
            CAPTURE_PHOTO_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    uploadPicture(bitmap)
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select a picture"),
            SELECT_PHOTO_REQUEST_CODE
        )
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        val photoUri = FileProvider.getUriForFile(
            requireContext(), "com.github.sdp.ratemyepfl.fileprovider", photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, CAPTURE_PHOTO_REQUEST_CODE)
    }

    private fun uploadPicture(bitmap: Bitmap) {
        val id = TimeUtils.timeStamp()
        viewModel.uploadPicture(ImageFile(id, bitmap))
        displayOnToast(requireContext(), "Your photo was uploaded successfully!")
    }

    private fun createImageFile(): File {
        val id = TimeUtils.timeStamp()
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(id, FirebaseImageStorage.FILE_EXTENSION, storageDir)
            .apply { currentPhotoPath = absolutePath }
    }
}