package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.AddClassViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClassFragment : Fragment(R.layout.fragment_add_class) {
    private lateinit var courseText: EditText
    private lateinit var roomText: EditText
    private lateinit var dayPicker: MaterialDayPicker

    private val addClassViewModel: AddClassViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        courseText = view.findViewById(R.id.courseClassInputText)
        roomText = view.findViewById(R.id.roomClassInputText)
        dayPicker = view.findViewById(R.id.dayPicker)

        dayPicker.disableDay(MaterialDayPicker.Weekday.SATURDAY)
        dayPicker.disableDay(MaterialDayPicker.Weekday.SUNDAY)
        dayPicker.firstDayOfWeek = MaterialDayPicker.Weekday.MONDAY

        dayPicker.setDaySelectionChangedListener {
            if (it.isEmpty()) {
                addClassViewModel.day.postValue(null)
            } else {
                addClassViewModel.day.postValue(it.first().ordinal - 1)
                Log.d("fojijfoijifo", it.first().ordinal.toString())
            }
        }

        courseText.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.selectCourseFragment)
        }

        roomText.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.selectRoomFragment)
        }

        addClassViewModel.course.observe(viewLifecycleOwner) {
            if (it != null) {
                courseText.setText(it.title, TextView.BufferType.EDITABLE)
            }
        }

        addClassViewModel.room.observe(viewLifecycleOwner) {
            if (it != null) {
                roomText.setText(it.name, TextView.BufferType.EDITABLE)
            }
        }

        dayPicker
    }
}