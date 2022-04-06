package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.PhotoAdapter
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewPictureFragment : Fragment(R.layout.fragment_room_review_picture) {

    companion object {
        /* Number of columns in the image grid */
        private const val NUM_COLUMNS = 2
    }

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecyclerView: RecyclerView

    private val viewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoRecyclerView = view.findViewById(R.id.photoRecyclerView)
        val gridLayoutManager =
            StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
        photoRecyclerView.layoutManager = gridLayoutManager

        photoAdapter = PhotoAdapter()
        photoRecyclerView.adapter = photoAdapter

        viewModel.photos.observe(viewLifecycleOwner) {
            it?.let {
                photoAdapter.setData(it.toMutableList())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePhotosList()
    }
}