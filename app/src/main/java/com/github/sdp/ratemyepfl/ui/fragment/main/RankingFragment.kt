package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.ui.adapter.post.OnClickListener
import com.github.sdp.ratemyepfl.ui.adapter.post.SubjectAdapter
import com.github.sdp.ratemyepfl.viewmodel.main.HomeViewModel
import com.github.sdp.ratemyepfl.viewmodel.main.RankingViewModel
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RankingFragment : Fragment(R.layout.fragment_ranking) {

    private lateinit var userTop1Picture: CircleImageView
    private lateinit var userTop1Name: TextView
    private lateinit var userTop1Karma: TextView

    private lateinit var userTop2Picture: CircleImageView
    private lateinit var userTop2Name: TextView
    private lateinit var userTop2Karma: TextView

    private lateinit var userTop3Picture: CircleImageView
    private lateinit var userTop3Name: TextView
    private lateinit var userTop3Karma: TextView

    @Inject
    lateinit var connectedUser: ConnectedUser

    private val viewModel by activityViewModels<RankingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePodium(view)
    }

    private fun initializePodium(view: View) {
        userTop1Picture = view.findViewById(R.id.userTop1Picture)
        userTop1Name = view.findViewById(R.id.userTop1Name)
        userTop1Karma = view.findViewById(R.id.userTop1Karma)

        userTop2Picture = view.findViewById(R.id.userTop2Picture)
        userTop2Name = view.findViewById(R.id.userTop2Name)
        userTop2Karma = view.findViewById(R.id.userTop2Karma)

        userTop3Picture = view.findViewById(R.id.userTop3Picture)
        userTop3Name = view.findViewById(R.id.userTop3Name)
        userTop3Karma = view.findViewById(R.id.userTop3Karma)

        viewModel.topUsersPictures.observe(viewLifecycleOwner) {
            it?.let {
                userTop1Picture.setImageBitmap(it[0]?.data)
                userTop2Picture.setImageBitmap(it[1]?.data)
                userTop3Picture.setImageBitmap(it[2]?.data)
            }
        }

        viewModel.topUsers.observe(viewLifecycleOwner) {
            it?.let {
                userTop1Name.text = it[0].username
                userTop1Karma.text = it[0].karma.toString()
                userTop2Name.text = it[1].username
                userTop2Karma.text = it[1].karma.toString()
                userTop3Name.text = it[2].username
                userTop3Karma.text = it[2].karma.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUsers()
    }
}