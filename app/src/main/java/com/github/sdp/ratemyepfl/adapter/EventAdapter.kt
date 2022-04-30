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
    private val registerListener: (Event) -> Boolean,
    private val unregisterListener: (Event) -> Unit
) :
    ListAdapter<Event, EventAdapter.EventViewHolder>(AdapterUtil.diffCallback<Event>()) {

    // Temporary solution but will change and will check database directly instead
    private var registerMap: Map<String, Boolean> = mapOf()

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
            // Check registration locally but in the future it will be recorded on the database
            // TODO(MODIFICATION)
            if (registerMap[event.getId()] == true) {
                registeredBehavior()
            } else {
                unregisteredBehavior()
            }

            registerButton.setOnClickListener {
                if (registerMap[event.getId()] == false) {
                    if (registerListener(event) && event.numParticipants < event.limitParticipants) {
                        registeredBehavior()
                        registerMap = registerMap.plus(Pair(event.getId(), true))
                    }
                } else {
                    unregisterListener(event)
                    unregisteredBehavior()
                    registerMap = registerMap.plus(Pair(event.getId(), false))
                }
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
        fun bind(event: Event, position: Int) {
            currentEvent = event
            eventTextView.text = event.toString()
            participantsTextView.text = event.showParticipation()

            if (!registerMap.containsKey(event.getId())) {
                registerMap = registerMap.plus(Pair(event.getId(), false))
            }
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
        holder.bind(event, position)
    }

    companion object {
        const val REGISTER = "Register"
        const val UNREGISTER = "Unregister"
    }
}