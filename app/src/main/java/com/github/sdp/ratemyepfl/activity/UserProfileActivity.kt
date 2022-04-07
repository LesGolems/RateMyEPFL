package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.ConnectedUserImpl
import com.github.sdp.ratemyepfl.database.ImageStorage
import com.github.sdp.ratemyepfl.database.UserDatabase
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
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), SELECT_IMAGE)
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

    // mediastore methods are deprecated but the replacement requires another API
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            SELECT_IMAGE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val photoUri = data.data
                    val source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    val image = ImageFile(currentUser.getUserId()!!, bitmap)
                    viewModel.changeProfilePicture(image)
                }
            }
        }
    }
}