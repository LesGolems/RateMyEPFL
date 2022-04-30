package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.ktx.utils.heatmaps.heatmapTileProviderWithData
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
open class DrawerActivity : AppCompatActivity() {
    private val profileViewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setUpProfile(drawerView: NavigationView) {
        val headerView = drawerView.getHeaderView(0)
        val pic: CircleImageView = headerView.findViewById(R.id.pictureDrawer)
        val username: TextView = headerView.findViewById(R.id.usernameDrawer)
        val email: TextView = headerView.findViewById(R.id.emailDrawer)

        profileViewModel.picture.observe(this){
            pic.setImageBitmap(it?.data)
        }
        profileViewModel.username.observe(this) {
            username.text = it
        }
        profileViewModel.email.observe(this){
            email.text = it
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.refreshProfile()
    }

}

