package com.github.sdp.ratemyepfl.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Class

class ClassAdapter(
    private val classes: List<Class>,
    private val onClickCourse: (String) -> Unit,
    private val onClickRoom: (String) -> Unit
) :
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    private lateinit var className: TextView
    private lateinit var room: TextView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        return ClassViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_class, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val item = classes[position]

        className = holder.view.findViewById(R.id.classname)
        className.text = item.name ?: "?"
        className.setOnClickListener {
            item.courseId?.let { it1 -> onClickCourse(it1) }
        }

        holder.view.findViewById<TextView>(R.id.teacher).text = item.teacher ?: "?"

        room = holder.view.findViewById(R.id.room)
        room.text = item.room ?: "?"
        room.setOnClickListener {
            item.room?.let { it1 -> onClickRoom(it1) }
        }

        holder.view.findViewById<TextView>(R.id.startTime).text = convert(item.start)
        holder.view.findViewById<TextView>(R.id.endTime).text = convert(item.end)

        // Increase item height for longer classes
        if (item.duration() > 1) {
            holder.view.layoutParams.height =
                (holder.view.context.resources.getDimension(R.dimen.classHeight) * item.duration()).toInt()
        }
    }

    override fun getItemCount(): Int = classes.size

    class ClassViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private fun convert(time: Int?): String {
        if (time == null) return "?"
        if (time < 12) {
            return "$time AM"
        } else if (time < 24) {
            return "$time PM"
        }
        return ""
    }
}