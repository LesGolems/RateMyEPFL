package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.ui.layout.LoadingCircleImageView
import com.github.sdp.ratemyepfl.viewmodel.main.RankingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RankingFragment : Fragment(R.layout.fragment_ranking) {

    private lateinit var userTop1Picture: LoadingCircleImageView
    private lateinit var userTop1Name: TextView
    private lateinit var userTop1Karma: TextView

    private lateinit var userTop2Picture: LoadingCircleImageView
    private lateinit var userTop2Name: TextView
    private lateinit var userTop2Karma: TextView

    private lateinit var userTop3Picture: LoadingCircleImageView
    private lateinit var userTop3Name: TextView
    private lateinit var userTop3Karma: TextView

    private lateinit var swipeRefresher: SwipeRefreshLayout

    @Inject
    lateinit var connectedUser: ConnectedUser

    private val viewModel by activityViewModels<RankingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePodium(view)

        swipeRefresher = view.findViewById(R.id.podiumSwipeRefresh)
        swipeRefresher.setOnRefreshListener {
            viewModel.refreshUsers()
            swipeRefresher.isRefreshing = false
        }
    }

    private fun initializePodium(view: View) {
        val userTop1PictureLayout: View = view.findViewById(R.id.userTop1Picture)
        userTop1Picture = LoadingCircleImageView(userTop1PictureLayout)
        userTop1Name = view.findViewById(R.id.userTop1Name)
        userTop1Karma = view.findViewById(R.id.userTop1Karma)
        val userTop2PictureLayout: View = view.findViewById(R.id.userTop2Picture)
        userTop2Picture = LoadingCircleImageView(userTop2PictureLayout)
        userTop2Name = view.findViewById(R.id.userTop2Name)
        userTop2Karma = view.findViewById(R.id.userTop2Karma)
        val userTop3PictureLayout: View = view.findViewById(R.id.userTop3Picture)
        userTop3Picture = LoadingCircleImageView(userTop3PictureLayout)
        userTop3Name = view.findViewById(R.id.userTop3Name)
        userTop3Karma = view.findViewById(R.id.userTop3Karma)

        viewModel.topUsersPictures.first.observe(viewLifecycleOwner) { image ->
            image.let {
                userTop1Picture.image.setImageBitmap(it.data)
            }
        }
        viewModel.topUsersPictures.second.observe(viewLifecycleOwner) { image ->
            image.let {
                userTop2Picture.image.setImageBitmap(it.data)
            }
        }
        viewModel.topUsersPictures.third.observe(viewLifecycleOwner) { image ->
            image.let {
                userTop3Picture.image.setImageBitmap(it.data)
            }
        }

        viewModel.topUsers.first.observe(viewLifecycleOwner) {
            it.let {
                userTop1Name.text = it.username
                userTop1Karma.text = it.karma.toString()
            }
        }

        viewModel.topUsers.first.observe(viewLifecycleOwner) {
            it.let {
                userTop2Name.text = it.username
                userTop2Karma.text = it.karma.toString()
            }
        }

        viewModel.topUsers.first.observe(viewLifecycleOwner) {
            it.let {
                userTop3Name.text = it.username
                userTop3Karma.text = it.karma.toString()
            }
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUsers()
        displayPictures()
    }

    fun displayPictures() {
        val flows = viewModel.getTop3Pictures()
        val defaultImage = userTop1Picture.getDefaultImage()
        viewModel.viewModelScope
            .launch {
                val picture = viewModel.topUsersPictures.first
                userTop1Picture.display(flows.first, {
                    picture.postValue(it)
                }) {
                    picture.postValue(defaultImage)
                }
            }

        viewModel.viewModelScope
            .launch {
                val picture = viewModel.topUsersPictures.second
                userTop1Picture.display(flows.second, {
                    picture.postValue(it)
                }) {
                    picture.postValue(defaultImage)
                }
            }

        viewModel.viewModelScope
            .launch {
                val picture = viewModel.topUsersPictures.third
                userTop1Picture.display(flows.third, {
                    picture.postValue(it)
                }) {
                    picture.postValue(defaultImage)
                }
            }
    }
}