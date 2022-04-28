package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.android.material.button.MaterialButton

class EventAdapter(private val onClick: (Event) -> Unit,
                   private val registerListener: () -> Unit) :
    ListAdapter<Event, EventAdapter.EventViewHolder>(AdapterUtil.diffCallback<Event>()) {

    private var list: List<Event> = listOf()

    fun submitData(data: List<Event>, commitCallback: (() -> Unit) = {}) {
        list = data.toList()
        submitList(list, commitCallback)
    }

    inner class EventViewHolder(eventView: View) :
        RecyclerView.ViewHolder(eventView) {

        private val eventTextView: TextView = eventView.findViewById(R.id.eventId)
        private val participantsTextView: TextView = eventView.findViewById(R.id.participants)
        private val registerButton: MaterialButton = eventView.findViewById(R.id.registerButton)
        private var currentEvent: Event? = null

        init {
            eventView.isClickable = true
            eventView.findViewById<LinearLayout>(R.id.eventItemLayout)
                .setOnClickListener {
                    currentEvent?.let {
                        onClick(it)
                    }
                }
            setUpRegisterButton()
        }

        /**
         * Set the listener of the register button
         */
        private fun setUpRegisterButton() {
            registerButton.setOnClickListener {
                registerButton.text = if (registerButton.text == REGISTER) UNREGISTER else REGISTER
                registerListener()
            }
        }

        /**
         * Bind event
         */
        fun bind(event: Event) {
            currentEvent = event
            eventTextView.text = event.toString()
            participantsTextView.text = event.showParticipation()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.event_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        const val REGISTER = "Register"
        const val UNREGISTER = "Unregister"
    }
}