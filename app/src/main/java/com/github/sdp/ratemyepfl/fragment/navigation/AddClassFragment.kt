package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.viewmodel.AddClassViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClassFragment : Fragment(R.layout.fragment_add_class) {
    private lateinit var courseText: EditText
    private lateinit var roomText: EditText
    private lateinit var dayPicker: MaterialDayPicker
    private lateinit var startHourPicker: NumberPicker
    private lateinit var endHourPicker: NumberPicker
    private lateinit var doneButton: Button
    private lateinit var addClassScrollView: ScrollView

    private val addClassViewModel: AddClassViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        courseText = view.findViewById(R.id.courseClassInputText)
        roomText = view.findViewById(R.id.roomClassInputText)
        dayPicker = view.findViewById(R.id.dayPicker)
        startHourPicker = view.findViewById(R.id.startTimePicker)
        endHourPicker = view.findViewById(R.id.endTimePicker)
        doneButton = view.findViewById(R.id.doneButton)
        addClassScrollView = view.findViewById(R.id.addClassScrollView)

        setUpHourPickers()
        setUpDayPicker()
        setUpCourseSelector()
        setUpRoomSelector()

        doneButton.setOnClickListener {
            try {
                userProfileViewModel.addClass(addClassViewModel.createClass())
                reset()
                Thread.sleep(1000) // Wait for DB to be updated
                Navigation.findNavController(requireView()).popBackStack()
            } catch (e : MissingInputException){
                displayMessage(e.message)
            }
        }
    }

    private fun reset(){
        addClassViewModel.room.postValue(null)
        addClassViewModel.course.postValue(null)
        addClassViewModel.day.postValue(null)
        addClassViewModel.startHour.postValue(0)
        addClassViewModel.endHour.postValue(0)
    }

    private fun setUpCourseSelector() {
        courseText.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.selectCourseFragment)
        }

        addClassViewModel.course.observe(viewLifecycleOwner) {
            if (it != null) {
                courseText.setText(it.title, TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun setUpRoomSelector() {
        roomText.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.selectRoomFragment)
        }

        addClassViewModel.room.observe(viewLifecycleOwner) {
            if (it != null) {
                roomText.setText(it.name, TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun setUpDayPicker() {
        dayPicker.disableDay(MaterialDayPicker.Weekday.SATURDAY)
        dayPicker.disableDay(MaterialDayPicker.Weekday.SUNDAY)
        dayPicker.firstDayOfWeek = MaterialDayPicker.Weekday.MONDAY

        dayPicker.setDaySelectionChangedListener {
            if (it.isEmpty()) {
                addClassViewModel.day.postValue(null)
            } else {
                addClassViewModel.day.postValue(it.first().ordinal - 1)
            }
        }
    }

    private fun setUpHourPickers() {
        startHourPicker.maxValue = 24
        endHourPicker.maxValue = 24

        endHourPicker.setOnValueChangedListener { _, _, newVal ->
            addClassViewModel.endHour.postValue(newVal)
        }

        startHourPicker.setOnValueChangedListener { _, _, newVal ->
            addClassViewModel.startHour.postValue(newVal)
        }
    }

    private fun displayMessage(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}