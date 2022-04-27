package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.RoomPictureAdapter
import com.github.sdp.ratemyepfl.viewmodel.ClassroomPictureViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewPictureFragment : Fragment(R.layout.fragment_room_review_picture) {

    companion object {
        /* Number of columns in the image grid */
        private const val NUM_COLUMNS = 2
    }

    private lateinit var pictureAdapter: RoomPictureAdapter
    private lateinit var pictureRecyclerView: RecyclerView

    private val pictureViewModel by activityViewModels<ClassroomPictureViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pictureRecyclerView = view.findViewById(R.id.pictureRecyclerView)
        val gridLayoutManager =
            StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
        pictureRecyclerView.layoutManager = gridLayoutManager

        pictureAdapter = RoomPictureAdapter()
        pictureRecyclerView.adapter = pictureAdapter

        pictureViewModel.pictures.observe(viewLifecycleOwner) {
            pictureAdapter.setData(it.toMutableList())
        }
    }

    override fun onResume() {
        super.onResume()
        pictureViewModel.updatePicturesList()
    }
}