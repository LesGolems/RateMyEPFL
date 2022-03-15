package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.R
import java.util.*

class ClassroomsAdapter(private val onClick: (Classroom) -> Unit) :
    ListAdapter<Classroom, ClassroomsAdapter.RoomViewHolder>(RoomDiffCallback),
    Filterable {

    private var list = mutableListOf<Classroom>()

    inner class RoomViewHolder(roomView: View) :
        RecyclerView.ViewHolder(roomView) {

        private val roomTextView: TextView = roomView.findViewById(R.id.room_id)
        private val reviewBut: Button = roomView.findViewById(R.id.reviewRoomButton)
        private var currentRoom: Classroom? = null

        init {
            reviewBut.setOnClickListener {
                currentRoom?.let {
                    onClick(it)
                }
            }
        }

        /* Bind room id. */
        fun bind(room: Classroom) {
            currentRoom = room
            roomTextView.text = room.id
        }
    }

    /* Creates and inflates view and return RoomViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_item,
                parent,
                false
            )
        )
    }

    /* Gets current room and uses it to bind view. */
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    fun setData(list: MutableList<Classroom>?) {
        this.list = list!!
        submitList(list)
    }

    override fun getFilter(): Filter {
        return roomSearchFilter
    }

    private val roomSearchFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val results = FilterResults()

            if (query.isNullOrEmpty()) {
                results.values = list
                results.count = list.size
            } else {
                val queryLower = query.toString().lowercase()
                val filteredList = mutableListOf<Classroom>()
                filteredList.addAll(list.filter {
                    it.id.lowercase().startsWith(queryLower)
                })
                results.values = filteredList
                results.count = filteredList.size
            }

            return results
        }

        override fun publishResults(query: CharSequence?, searchResults: FilterResults?) {
            submitList(searchResults!!.values as MutableList<Classroom>)
        }
    }

    fun sortAlphabetically() {
        val sortedList = mutableListOf<Classroom>()
        sortedList.addAll(list)
        sortedList.sortBy { it.id }
        submitList(sortedList)
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