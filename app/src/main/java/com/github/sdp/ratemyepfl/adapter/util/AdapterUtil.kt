package com.github.sdp.ratemyepfl.adapter.util

import androidx.recyclerview.widget.DiffUtil

object AdapterUtil {

    fun <T> diffCallback() = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
}