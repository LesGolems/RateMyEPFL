package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.github.sdp.ratemyepfl.R
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditEventActitivity : AppCompatActivity() {

    private lateinit var eventScrollView: ScrollView
    private lateinit var eventNameText: TextInputEditText
    private lateinit var limitPicker: NumberPicker
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var mapContainer: FragmentContainerView
    private lateinit var cancelButton: Button
    private lateinit var doneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        eventScrollView = findViewById(R.id.eventScrollView)

        mapContainer = findViewById(R.id.mapContainer)
        
    }
}