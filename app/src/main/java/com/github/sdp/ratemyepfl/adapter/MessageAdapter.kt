package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.messaging.Message
import com.github.sdp.ratemyepfl.model.user.User

class MessageAdapter(
    private val messages: List<Message>,
    private val recipients: Map<String, User>,
    private val recipientPictures: Map<String, ImageFile>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val OUTGOING_TYPE = 0
        const val INCOMING_TYPE = 1
    }

    inner class IncomingMessageViewHolder(messageView: View)
        : RecyclerView.ViewHolder(messageView)
    {
        private val senderName : TextView = messageView.findViewById(R.id.incoming_message_sender)
        private val senderThumbnail : ImageView = messageView.findViewById(R.id.img_thumbnail)
        private val timestamp : TextView = messageView.findViewById(R.id.incoming_message_timestamp)
        private val messageBody : TextView = messageView.findViewById(R.id.incoming_message_body)

        fun bind(message: Message, sender: User?) {
            sender.let {
                senderName.setText(it?.username)
                senderThumbnail.setImageBitmap(recipientPictures[it?.uid]?.data)
                timestamp.setText(message.timestamp.toString())
                messageBody.setText(message.content)
            }
        }
    }

    inner class OutgoingMessageViewHolder(messageView: View)
        : RecyclerView.ViewHolder(messageView)
    {
        private val senderName : TextView = messageView.findViewById(R.id.outgoing_message_sender)
        private val messageBody : TextView = messageView.findViewById(R.id.outgoing_message_body)
        private val timestamp : TextView = messageView.findViewById(R.id.outgoing_message_timestamp)

        fun bind(message: Message) {
            senderName.setText("You")
            messageBody.setText(message.content)
            timestamp.setText(message.timestamp.toString())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (recipients.containsKey(messages[position].senderId)) {
            INCOMING_TYPE
        } else {
            OUTGOING_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == OUTGOING_TYPE) {
            OutgoingMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.outgoing_message,
                    parent,
                    false
                )
            )
        } else {
            IncomingMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.incoming_message,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (getItemViewType(position) == INCOMING_TYPE) {
            val incomingMessageHolder = holder as IncomingMessageViewHolder
            incomingMessageHolder.bind(message, recipients[message.senderId])
        } else {
            val outgoingMessageHolder = holder as OutgoingMessageViewHolder
            outgoingMessageHolder.bind(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}