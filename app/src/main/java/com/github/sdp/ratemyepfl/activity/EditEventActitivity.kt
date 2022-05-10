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

        eventScrollView = findViewById(R.id.eventScrollView)
        eventScrollView.addInterceptScrollView(mapContainer)

        limitPicker = findViewById(R.id.limitPicker)
        limitPicker.maxValue = MAX_LIMIT_PART

        eventNameText = findViewById(R.id.editEventName)

        datePicker = findViewById(R.id.datePicker)

        timePicker = findViewById(R.id.timePicker)

        cancelButton = findViewById(R.id.cancelButton)

        doneButton = findViewById(R.id.doneButton)
    }
}