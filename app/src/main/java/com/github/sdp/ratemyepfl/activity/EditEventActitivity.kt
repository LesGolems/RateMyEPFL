package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.layout.CustomScrollView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate


@AndroidEntryPoint
class EditEventActitivity : AppCompatActivity() {

    private lateinit var eventScrollView: CustomScrollView
    private lateinit var eventNameText: TextInputEditText
    private lateinit var limitPicker: NumberPicker
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var mapContainer: FragmentContainerView
    private lateinit var cancelButton: Button
    private lateinit var doneButton: Button

    companion object {
        private const val MAX_LIMIT_PART = 10000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        mapContainer = findViewById(R.id.mapContainer)
        eventNameText = findViewById(R.id.editEventName)

        eventScrollViewInit()
        limitPickerInit()
        datePickerInit()
        timePickerInit()
        cancelButtonInit()
        doneButtonInit()
    }

    private fun limitPickerInit() {
        limitPicker = findViewById(R.id.limitPicker)
        limitPicker.maxValue = MAX_LIMIT_PART
    }

    private fun datePickerInit() {
        val currentDate = LocalDate.now()
        datePicker = findViewById(R.id.datePicker)
        datePicker.updateDate(currentDate.year, currentDate.monthValue-1, currentDate.dayOfMonth)
    }

    private fun timePickerInit() {
        timePicker = findViewById(R.id.timePicker)
        timePicker.setIs24HourView(true)
    }

    private fun eventScrollViewInit() {
        eventScrollView = findViewById(R.id.eventScrollView)
        eventScrollView.addInterceptScrollView(mapContainer)
    }

    private fun cancelButtonInit() {
        cancelButton = findViewById(R.id.cancelButton)
    }

    private fun doneButtonInit() {
        doneButton = findViewById(R.id.doneButton)
    }
}