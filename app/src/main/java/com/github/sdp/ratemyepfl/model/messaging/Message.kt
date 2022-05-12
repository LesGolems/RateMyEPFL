package com.github.sdp.ratemyepfl.model.messaging

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDateTime

data class Message(
    @DocumentId
    val id: String = "",
    @ServerTimestamp
    val timestamp: LocalDateTime? = null,
    @PropertyName("sender_id")
    val senderId: String = "",
    @Exclude
    val channelId: String = "",
    @PropertyName("content")
    val content: String = ""
)