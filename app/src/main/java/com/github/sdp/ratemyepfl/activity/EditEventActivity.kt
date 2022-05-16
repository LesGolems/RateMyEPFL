package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.fragment.main.MapFragment
import com.github.sdp.ratemyepfl.fragment.review.AddReviewFragment
import com.github.sdp.ratemyepfl.layout.CustomScrollView
import com.github.sdp.ratemyepfl.viewmodel.main.EditEventViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate


@AndroidEntryPoint
class EditEventActivity : AppCompatActivity() {

    private lateinit var eventScrollView: CustomScrollView
    private lateinit var eventNameText: TextInputEditText
    private lateinit var limitPicker: NumberPicker
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var mapContainer: FragmentContainerView
    private lateinit var cancelButton: Button
    private lateinit var doneButton: Button

    private val editEventViewModel: EditEventViewModel by viewModels()

    companion object {
        const val EXTRA_EVENT_ID: String =
            "com.github.sdp.extra_event_id"
        const val EXTRA_EVENT_TITLE: String =
            "com.github.sdp.extra_event_title"
        const val EXTRA_EVENT_LIM_PART: String =
            "com.github.sdp.extra_event_lim_part"
        const val EXTRA_EVENT_TIME: String =
            "com.github.sdp.extra_event_time"
        const val EXTRA_EVENT_DATE: String =
            "com.github.sdp.extra_event_date"
        const val EXTRA_EVENT_LOCATION: String =
            "com.github.sdp.extra_event_location"
        const val EXTRA_IS_NEW_EVENT: String =
            "com.github.sdp.extra_is_new_event"

        private const val MAX_LIMIT_PART = 10000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        mapContainerInit()
        nameTextInit()
        eventScrollViewInit()
        limitPickerInit()
        datePickerInit()
        timePickerInit()
        cancelButtonInit()
        doneButtonInit()
    }

    private fun mapContainerInit() {
        mapContainer = findViewById(R.id.mapContainer)
        val mapFragment = mapContainer.getFragment<MapFragment>()
        mapFragment.onClickLocation.observeForever { latLng ->
            editEventViewModel.setLocation(doubleArrayOf(latLng.latitude, latLng.longitude))
        }
    }

    private fun nameTextInit() {
        eventNameText = findViewById(R.id.editEventName)
        eventNameText.addTextChangedListener(AddReviewFragment.onTextChangedTextWatcher { text, _, _, _ ->
            editEventViewModel.setTitle(text?.toString())
        })

        if (!editEventViewModel.isNewEvent) {
            editEventViewModel.eventTitle?.let {
                eventNameText.setText(it)
            }
        }
    }

    private fun limitPickerInit() {
        limitPicker = findViewById(R.id.limitPicker)
        limitPicker.maxValue = MAX_LIMIT_PART
        limitPicker.setOnValueChangedListener { _, _, newVal ->
            editEventViewModel.setLimPart(newVal)
        }

        if (!editEventViewModel.isNewEvent) {
            editEventViewModel.eventLimPart?.let {
                limitPicker.value = it
            }
            editEventViewModel.eventLimPart?.let {
                limitPicker.minValue = it
            }
        }
    }

    private fun datePickerInit() {
        val currentDate = LocalDate.now()
        var dateValues = IntArray(3)
        datePicker = findViewById(R.id.datePicker)
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            editEventViewModel.setDate(intArrayOf(year, monthOfYear, dayOfMonth))
        }

        if (!editEventViewModel.isNewEvent) {
            editEventViewModel.eventDate?.let {
                dateValues = it
            }
        } else {
            dateValues[0] = currentDate.year
            dateValues[1] = currentDate.monthValue
            dateValues[2] = currentDate.dayOfMonth
        }
        editEventViewModel.setDate(dateValues)
        datePicker.updateDate(dateValues[0], dateValues[1], dateValues[2])
    }

    private fun timePickerInit() {
        timePicker = findViewById(R.id.timePicker)
        timePicker.setIs24HourView(true)
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            editEventViewModel.setTime(intArrayOf(hourOfDay, minute))
        }

        if (!editEventViewModel.isNewEvent) {
            editEventViewModel.eventTime?.let {
                timePicker.hour = it[0]
                timePicker.minute = it[1]
            }
        }
        editEventViewModel.setTime(intArrayOf(timePicker.hour, timePicker.minute))
    }

    private fun eventScrollViewInit() {
        eventScrollView = findViewById(R.id.eventScrollView)
        eventScrollView.addInterceptScrollView(mapContainer)
    }

    private fun cancelButtonInit() {
        cancelButton = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            comebackToMain()
        }
    }

    private fun doneButtonInit() {
        doneButton = findViewById(R.id.doneButton)
        doneButton.setOnClickListener {
            addEvent()
        }
    }

    /**
     *  Adds the event to the database
     */
    private fun addEvent() {
        try {
            editEventViewModel.submitEvent()
            displayMessage(getString(R.string.event_sent_text))
            comebackToMain()
        } catch (due: DisconnectedUserException) {
            displayMessage(due.message)
        } catch (e: Exception) {
            if (eventNameText.text.isNullOrEmpty()) {
                eventNameText.error = e.message
                displayMessage(e.message)
            } else {
                displayMessage(e.message)
            }
        }
    }

    private fun displayMessage(message: String?) {
        if (message != null) {
            Snackbar.make(eventScrollView, message, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.editEventButtons)
                .show()
        }
    }

    private fun comebackToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}