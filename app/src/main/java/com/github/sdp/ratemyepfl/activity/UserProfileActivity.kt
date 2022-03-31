package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.github.sdp.ratemyepfl.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class UserProfileActivity : AppCompatActivity() {

    val SELECT_IMAGE = 1044

    private lateinit var profilePicture: CircleImageView
    private lateinit var cameraIcon: CircleImageView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        profilePicture = findViewById(R.id.profile_image)

        val storageRef = FirebaseStorage.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            storageRef.child("images/user_pictures/")
                .child(currentUser.uid + ".jpg")
                .getBytes(1024*1024)
                .addOnSuccessListener {
                    it?.let {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        profilePicture.setImageBitmap(bitmap)
                    }
                }
                .addOnFailureListener{
                    it.printStackTrace()
                }
        }

        cameraIcon = findViewById(R.id.modify_profile_image_button)
        emailTextView = findViewById(R.id.emailText)

        emailTextView.setText(FirebaseAuth.getInstance().currentUser?.displayName)

        cameraIcon.setOnClickListener(updatePicture)
    }

    val updatePicture = View.OnClickListener {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), SELECT_IMAGE)
    }

    @RequiresApi(Build.VERSION_CODES.P) // mediastore methods are deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            SELECT_IMAGE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val photoUri = data.data
                    val source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (currentUser != null) {
                        val storageReference = FirebaseStorage.getInstance().reference
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        storageReference.child("images/")
                            .child("user_pictures/")
                            .child(currentUser.uid + ".jpg")
                            .putBytes(baos.toByteArray())
                            .addOnSuccessListener {
                                profilePicture.setImageBitmap(bitmap)
                            }
                            .addOnFailureListener{
                                it.printStackTrace()
                            }
                    }
                }
            }
        }
    }
}