package com.github.sdp.ratemyepfl.model.messaging

import com.github.sdp.ratemyepfl.database.ChannelListener
import com.github.sdp.ratemyepfl.database.IChannel
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

data class Channel(
    @DocumentId
    override val id: String = "",
    @PropertyName("title")
    override var title: String = "",
    @PropertyName("members")
    override var members: List<String> = listOf(),
    @PropertyName("last_message")
    override var lastMessage: Message? = null
) : IChannel {

    private val channelRef = FirebaseFirestore.getInstance().collection("channels").document(id)
    private val threadRef = channelRef.collection("thread")
    private val listeners: ArrayList<ListenerRegistration> = ArrayList()

    init {
        // Listen for real-time modifications of database document linked to this object
        channelRef.addSnapshotListener { value, _ ->
            if (value != null) {
                val update = value.toObject(Channel::class.java)
                if (update != null) {
                    this.title = update.title
                    this.members = update.members
                    this.lastMessage = update.lastMessage
                }
            }
        }

        // Listen for new message on the channel discussion thread and set it as last message
        threadRef.addSnapshotListener { value, _ ->
            if (value != null) {
                for (dc in value.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        lastMessage = dc.document.toObject(Message::class.java)
                    }
                }
            }
        }
    }

    override suspend fun post(message: Message) {
        threadRef.add(message).await()
    }

    override fun startListening(listener: ChannelListener) {
        listeners.add(threadRef.addSnapshotListener { value, _ ->
            if (value != null) {
                val newMessages = value.documents.mapNotNull { it.toObject(Message::class.java) }
                listener.onMessagesReceived(newMessages)
            }
        })
    }

    override fun stopListening() {
        listeners.forEach { it.remove() }
    }

}


