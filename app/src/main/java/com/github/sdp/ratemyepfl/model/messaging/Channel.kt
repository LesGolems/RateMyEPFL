package com.github.sdp.ratemyepfl.model.messaging

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Channel(
    @DocumentId
    val id: String = "",
    @PropertyName("title")
    val title: String = "",
    @PropertyName("members")
    val members: List<String>,
    @PropertyName("last_message")
    val lastMessage: Message,
)