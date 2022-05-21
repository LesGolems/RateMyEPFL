package com.github.sdp.ratemyepfl.ui.fragment.profile

import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.ui.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.ui.fragment.main.ClassroomTabFragment
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.profile.AddClassViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * List of rooms but changing the onClick behaviour
 */
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