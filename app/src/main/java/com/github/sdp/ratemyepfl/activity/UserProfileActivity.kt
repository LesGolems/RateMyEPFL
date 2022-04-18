package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    val SELECT_IMAGE = 1044

    private lateinit var profilePicture: CircleImageView
    private lateinit var cameraIcon: CircleImageView
    private lateinit var emailText: EditText
    private lateinit var usernameText: EditText
    private lateinit var modifyButton: ImageButton

    @Inject
    lateinit var currentUser : ConnectedUser

    private val viewModel by viewModels<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        profilePicture = findViewById(R.id.profile_image)
        cameraIcon = findViewById(R.id.modify_profile_image_button)
        emailText = findViewById(R.id.emailText)
        usernameText = findViewById(R.id.username_text)
        modifyButton = findViewById(R.id.modify_profile_button)

        viewModel.picture().observe(this) {
            profilePicture.setImageBitmap(it?.data)
        }

        viewModel.username().observe(this) {
            usernameText.setText(it.orEmpty())
        }

        viewModel.email().observe(this) {
            emailText.setText(it.orEmpty())
        }

        enableModifications(false)

        cameraIcon.setOnClickListener(updatePicture)
        modifyButton.setOnClickListener(updateProfile)
    }

    val updatePicture = View.OnClickListener {
        getContent.launch("image/*")
    }

    @RequiresApi(Build.VERSION_CODES.P)
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri: Uri? ->
            Toast.makeText(this, "fijfji", Toast.LENGTH_SHORT).show()
            val source = ImageDecoder.createSource(this.contentResolver, uri!!)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val image = ImageFile(currentUser.getUserId()!!, bitmap)
            viewModel.changeProfilePicture(image)
    }

    val updateProfile = View.OnClickListener {
        if (!it.isActivated) {
                enableModifications(true)
        } else {
            try {
                viewModel.changeUsername(usernameText.text.toString())
                viewModel.changeEmail(emailText.text.toString())
                viewModel.submitChanges()
            } catch (e: Exception) {
                viewModel.discardChanges()
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
            enableModifications(false)
        }
    }

    private fun enableModifications(boolean: Boolean) {
        cameraIcon.isEnabled = boolean
        if (boolean) {
            cameraIcon.visibility = View.VISIBLE
        } else {
            cameraIcon.visibility = View.GONE
        }
        emailText.isEnabled = boolean
        usernameText.isEnabled = boolean
        modifyButton.isActivated = boolean
    }

}