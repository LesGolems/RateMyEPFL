package com.github.sdp.ratemyepfl.utils

import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DiffUtil
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter

class ListActivityUtils {

    companion object {
        fun setUpSearchView(menu: Menu?, adapter: ReviewableAdapter, searchId: Int) {
            val searchItem = menu!!.findItem(searchId)
            val searchView = searchItem.actionView as SearchView
            searchView.maxWidth = Int.MAX_VALUE
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return true
                }
            })
        }

        /*
        fun createOnScrollListener(
            onScrollUp: () -> Unit,
            onScrollDown: () -> Unit
        ): RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(p0: RecyclerView, p1: Int) {}

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        onScrollDown()
                    } else {
                        onScrollUp()
                    }
                }
            }
        */
        fun <T> diffCallback() = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
    }
}