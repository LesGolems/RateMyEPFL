package com.github.sdp.ratemyepfl.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.viewmodel.AddRoomPictureViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraX"
        private const val FILENAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    private lateinit var takePhotoButton: Button
    private lateinit var previewView: PreviewView
    private lateinit var photoImageView: ImageView
    private lateinit var uploadButton: Button

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private val viewModel: AddRoomPictureViewModel by viewModels()

    private lateinit var roomId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        roomId = intent.getStringExtra("ID").toString()

        // Request camera permissions
        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        previewView = findViewById(R.id.previewView)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        takePhotoButton.setOnClickListener { takePhoto() }

        photoImageView = findViewById(R.id.photoImageView)
        uploadButton = findViewById(R.id.uploadButton)

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // Permissions are already granted
            if (allPermissionGranted()) {
                startCamera()
            } else {
                // Permissions were not granted
                Toast.makeText(
                    this, "Permissions not granted by the user", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun allPermissionGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of the camera to the LifecycleOwner within the app's process
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview of the camera
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }


            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(Surface.ROTATION_90)
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                )

            } catch (e: Exception) {
                // e.g. if the app is no longer in focus
                Log.e(TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this)) // An Executor that runs on main thread
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        // null if user taps the photo button before image capture is set up
        val imageCapture = imageCapture ?: return

        // Create time stamped name
        val imageId = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    previewView.visibility = View.GONE
                    takePhotoButton.visibility = View.GONE

                    val bitmap = rotateImage(imageProxyToBitmap(image), 90)
                    photoImageView.visibility = View.VISIBLE
                    photoImageView.setImageBitmap(bitmap)

                    uploadButton.visibility = View.VISIBLE
                    uploadButton.setOnClickListener {
                        viewModel.submitPicture(ImageFile(imageId, bitmap), roomId)
                    }
                    image.close()
                }
            }
        )

        /*val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageId)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        // This object is where we can specify things about how we want our output to be.
        // We want the output saved in the MediaStore so other apps could display it, so add our MediaStore entry.
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(e: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${e.message}", e)
                }
            }
        )*/
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val byteArray = ByteArray(buffer.remaining())
        buffer.get(byteArray)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedImg
    }
}