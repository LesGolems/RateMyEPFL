package com.github.sdp.ratemyepfl.model.messaging

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    @DocumentId
    val id: String = "",
    @ServerTimestamp
    val timestamp: Date? = null,
    @PropertyName("sender_id")
    val senderId: String = "",
    @PropertyName("content")
    val content: String = ""
)