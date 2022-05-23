package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.model.messaging.Message

interface IChannel {

    val id: String

    var title: String

    var members: List<String>

    var lastMessage: Message?

    suspend fun post(message: Message)

    fun startListening(listener: ChannelListener)

    fun stopListening()

}