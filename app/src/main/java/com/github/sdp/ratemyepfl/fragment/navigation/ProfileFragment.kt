package com.github.sdp.ratemyepfl.fragment.navigation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
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

        setUpObservers()

        enableModifications(false)

        cameraIcon.setOnClickListener(updatePicture)
        modifyButton.setOnClickListener(updateProfile)
    }

    private fun setUpObservers(){
        viewModel.picture.observe(viewLifecycleOwner) {
            profilePicture.setImageBitmap(it?.data)
        }

        viewModel.username.observe(viewLifecycleOwner) {
            if(it != null){
                usernameText.setText(it.orEmpty())
            }else{
                usernameText.setText(getString(R.string.visitor))
            }
        }

        viewModel.email.observe(viewLifecycleOwner) {
            if(it != null){
                emailText.setText(it.orEmpty())
            }else{
                emailText.setText(getString(R.string.not_logged_in_text))
            }
        }
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
                    sideBarViewModel.user.postValue(
                        sideBarViewModel.user.value?.copy(
                            email = viewModel.newEmail,
                            username = viewModel.newUsername
                        )
                    )
                    sideBarViewModel.picture.postValue(viewModel.picture.value)
                } catch (e: Exception) {
                    viewModel.discardChanges()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                enableModifications(false)
            }
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

