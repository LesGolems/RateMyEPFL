package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.AddClassViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectRoomFragment : ClassroomTabFragment() {
    override val reviewableAdapter = ReviewableAdapter { t -> submitRoom(t) }
    private val addClassViewModel: AddClassViewModel by activityViewModels()

    private fun submitRoom(room: Reviewable) {
        val r = room as Classroom
        addClassViewModel.room.postValue(r)
        Navigation.findNavController(requireView()).popBackStack()
    }

}