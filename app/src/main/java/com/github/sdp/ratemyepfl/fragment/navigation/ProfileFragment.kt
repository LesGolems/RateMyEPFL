package com.github.sdp.ratemyepfl.fragment.navigation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        val SELECT_IMAGE = 1044
    }

    private lateinit var profilePicture: CircleImageView
    private lateinit var cameraIcon: CircleImageView
    private lateinit var emailText: EditText
    private lateinit var usernameText: EditText
    private lateinit var modifyButton: ImageButton

    @Inject
    lateinit var currentUser: ConnectedUser

    private val viewModel by viewModels<UserProfileViewModel>()
    private val sideBarViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePicture = view.findViewById(R.id.profile_image)
        cameraIcon = view.findViewById(R.id.modify_profile_image_button)
        emailText = view.findViewById(R.id.emailText)
        usernameText = view.findViewById(R.id.username_text)
        modifyButton = view.findViewById(R.id.modify_profile_button)

        viewModel.picture.observe(viewLifecycleOwner) {
            profilePicture.setImageBitmap(it?.data)
        }

        viewModel.username.observe(viewLifecycleOwner) {
            usernameText.setText(it.orEmpty())
        }

        viewModel.email.observe(viewLifecycleOwner) {
            emailText.setText(it.orEmpty())
        }

        enableModifications(false)

        cameraIcon.setOnClickListener(updatePicture)
        modifyButton.setOnClickListener(updateProfile)
    }

    val updatePicture = View.OnClickListener {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), SELECT_IMAGE)
    }

    val updateProfile = View.OnClickListener {
        if (currentUser.isLoggedIn()) {
            if (!it.isActivated) {
                enableModifications(true)
            } else {
                try {
                    viewModel.changeUsername(usernameText.text.toString())
                    viewModel.changeEmail(emailText.text.toString())
                    viewModel.submitChanges()
                    sideBarViewModel.user.postValue(userFromViewModel())
                    sideBarViewModel.picture.postValue(viewModel.picture.value)
                } catch (e: Exception) {
                    viewModel.discardChanges()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                enableModifications(false)
            }
        }
    }

    /**
     * Creates user from view model information
     */
    private fun userFromViewModel(): User? {
        val id = viewModel.currentUser.getUserId()
        val email = viewModel.newEmail
        val picture = viewModel.picture.value
        val username = viewModel.newUsername
        val karma = viewModel.karma.value
        val timetable = viewModel.timetable.value

        if (id != null && email != null && picture != null && username != null && karma != null && timetable != null) {
            return User(id, username, email, karma, timetable)
        }
        return null
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

        when (requestCode) {
            SELECT_IMAGE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val photoUri = data.data
                    val source =
                        ImageDecoder.createSource(requireActivity().contentResolver, photoUri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    val image = ImageFile(currentUser.getUserId()!!, bitmap)
                    try {
                        viewModel.changeProfilePicture(image)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

