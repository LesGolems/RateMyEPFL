package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.ui.layout.LoadingCircleImageView
import com.github.sdp.ratemyepfl.viewmodel.main.RankingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
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
            refreshUsers()
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

        setupObservers()
    }

    private fun setupObservers() {
        val firstPicture = viewModel.topUsersPictures.first
        val firstUser = viewModel.topUsers.first
        setUpNthPicture(firstPicture, userTop1Picture)
        setUpNthUser(firstUser, userTop1Name, userTop1Karma)

        val secondPicture = viewModel.topUsersPictures.second
        val secondUser = viewModel.topUsers.second
        setUpNthPicture(secondPicture, userTop2Picture)
        setUpNthUser(secondUser, userTop2Name, userTop2Karma)

        val thirdPicture = viewModel.topUsersPictures.third
        val thirdUser = viewModel.topUsers.third
        setUpNthPicture(thirdPicture, userTop3Picture)
        setUpNthUser(thirdUser, userTop3Name, userTop3Karma)
    }

    private fun setUpNthPicture(nthPicture: MutableLiveData<ImageFile>,
                                nthCircleImageView: LoadingCircleImageView) {
        nthPicture.observe(viewLifecycleOwner) { image ->
            image.let {
                nthCircleImageView.image.setImageBitmap(it.data)
            }
        }
    }

    private fun setUpNthUser(nthUser: MutableLiveData<User>, nthNameView: TextView,
                             nthKarmaView: TextView) {
        nthUser.observe(viewLifecycleOwner) {
            it.let {
                nthNameView.text = it.username
                nthKarmaView.text = it.karma.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUsers()
    }

    private fun refreshUsers() {
        viewModel.viewModelScope
            .launch {
                viewModel.refreshUsers().run {
                    val defaultImage = userTop1Picture.getDefaultImage()
                    val firstImage = viewModel.getPicture(this.first)
                    viewModel.viewModelScope.launch {
                        val picture = viewModel.topUsersPictures.first
                        refreshPicture(picture, firstImage, defaultImage)
                    }
                    val secondImage = viewModel.getPicture(this.second)
                    viewModel.viewModelScope.launch {
                        val picture = viewModel.topUsersPictures.second
                        refreshPicture(picture, secondImage, defaultImage)
                    }
                    val thirdImage = viewModel.getPicture(this.third)
                    viewModel.viewModelScope.launch {
                        val picture = viewModel.topUsersPictures.third
                        refreshPicture(picture, thirdImage, defaultImage)
                    }
                }
            }
    }

    private suspend fun refreshPicture(nthPicture: MutableLiveData<ImageFile>,
                                       nthImage: Flow<ImageFile>,
                                       defaultImage: ImageFile) {
        userTop1Picture.display(nthImage, {
            nthPicture.postValue(it)
        }) {
            nthPicture.postValue(defaultImage)
        }
    }
}