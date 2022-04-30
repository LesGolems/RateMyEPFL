package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.items.Event
import com.google.android.material.button.MaterialButton

class EventAdapter(
    private val onClick: (Event) -> Unit,
    private val registrationListener: (Event) -> Unit
) :
    ListAdapter<Event, EventAdapter.EventViewHolder>(AdapterUtil.diffCallback<Event>()) {

    private var uid: String? = null

    inner class EventViewHolder(eventView: View) :
        RecyclerView.ViewHolder(eventView) {

        private val eventTextView: TextView = eventView.findViewById(R.id.eventId)
        private val participantsTextView: TextView = eventView.findViewById(R.id.participants)
        private val registerButton: MaterialButton = eventView.findViewById(R.id.registerButton)
        private var currentEvent: Event? = null

        init {
            eventView.isClickable = true
            eventView.findViewById<RelativeLayout>(R.id.eventItemLayout)
                .setOnClickListener {
                    currentEvent?.let {
                        onClick(it)
                    }
                }
        }

        /**
         * Set the listener of the register button
         */
        private fun setUpRegisterButton(event: Event) {
            if (event.participants.contains(uid)) {
                registeredBehavior()
            } else {
                unregisteredBehavior()
            }

            registerButton.setOnClickListener {
                registrationListener(event)
            }
        }

        /**
         * Set the correct info about the button when the user is registered
         */
        private fun registeredBehavior() {
            registerButton.text = UNREGISTER
            registerButton.setIconResource(R.drawable.ic_logout_black_24dp)
        }

        /**
         * Set the correct info about the button when the user is unregistered
         */
        private fun unregisteredBehavior() {
            registerButton.text = REGISTER
            registerButton.setIconResource(R.drawable.ic_login_black_24dp)
        }

        /**
         * Bind event
         */
        fun bind(event: Event) {
            currentEvent = event
            eventTextView.text = event.toString()
            participantsTextView.text = event.showParticipation()

            setUpRegisterButton(event)
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

    fun setUserId(uid: String?) {
        this.uid = uid
    }

    companion object {
        const val REGISTER = "Register"
        const val UNREGISTER = "Unregister"
    }
}