package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.PhotoAdapter
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewPictureFragment : Fragment(R.layout.fragment_room_review_picture) {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecyclerView: RecyclerView

    private val viewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoRecyclerView = view.findViewById(R.id.photoRecyclerView)
        val gridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        photoRecyclerView.layoutManager = gridLayoutManager

        photoAdapter = PhotoAdapter()
        photoRecyclerView.adapter = photoAdapter

        viewModel.getPhotos().observe(viewLifecycleOwner) {
            it?.let {
                photoAdapter.setData(it.toMutableList())
            }
        }
    }
}