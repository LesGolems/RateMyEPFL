package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Class

class ClassAdapter(val day: String, private val classes: List<Class>) :
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        return ClassViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_class, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val item = classes[position]

        holder.view.findViewById<TextView>(R.id.classname).text = item.name ?: "?"
        holder.view.findViewById<TextView>(R.id.teacher).text = item.teacher ?: "?"
        holder.view.findViewById<TextView>(R.id.room).text = item.room?.id ?: "?"
        holder.view.findViewById<TextView>(R.id.startTime).text = convert(item.start)
        holder.view.findViewById<TextView>(R.id.endTime).text = convert(item.end)

        // Increase item height for longer classes
        if (item.duration > 1) {
            holder.view.layoutParams.height =
                (holder.view.context.resources.getDimension(R.dimen.classheight) * 2).toInt()
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