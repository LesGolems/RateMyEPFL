package com.github.sdp.ratemyepfl.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.Classroom
import com.github.sdp.ratemyepfl.R

/**
 * [RecyclerView.Adapter] that can display a [Classroom].
 */
class ClassroomsAdapter(private val onClick: (Classroom) -> Unit) :
    ListAdapter<Classroom, ClassroomsAdapter.RoomViewHolder>(RoomDiffCallback) {//RecyclerView.Adapter<ClassroomsAdapter.RoomViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class RoomViewHolder(roomView: View) :
        RecyclerView.ViewHolder(roomView) {

        private val roomTextView: TextView = roomView.findViewById(R.id.room_id)
        private var currentRoom: Classroom? = null

        val button = roomView.findViewById<Button>(R.id.reviewRoomButton).setOnClickListener {
            currentRoom?.let {
                onClick(it)
            }
        }

        /* Bind room id and name. */
        fun bind(room: Classroom) {
            currentRoom = room
            roomTextView.text = room.id
        }
    }
    /*inner class RoomViewHolder(binding: View) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }*/

    // Create new views (invoked by the layout manager)
    /* Creates and inflates view and return RoomViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_item,
                parent,
                false
            )
        )

        /*return RoomViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context), // R.layout.room_item
                parent,
                false
            )
        )*/
    }

    /* Gets current room and uses it to bind view. */
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position) //rooms[position]
        holder.bind(room)
        /*holder.idView.text = room.id
        holder.contentView.text = room.name*/
    }

}

object RoomDiffCallback : DiffUtil.ItemCallback<Classroom>() {
    override fun areItemsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
        return oldItem.id == newItem.id
    }
}