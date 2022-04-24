package com.github.sdp.ratemyepfl.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Class

class ClassAdapter(val day: String, private val classes: List<Class>) : RecyclerView.Adapter<ClassAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO()
    }

    override fun getItemCount(): Int = classes.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}