package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.model.messaging.Message

fun interface ChannelListener {
    fun onMessagesReceived(messages: List<Message>)
}