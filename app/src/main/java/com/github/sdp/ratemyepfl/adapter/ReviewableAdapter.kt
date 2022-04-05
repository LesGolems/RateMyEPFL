package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil

class ReviewableAdapter(private val onClick: (Reviewable) -> Unit) :
    ListAdapter<Reviewable, ReviewableAdapter.ReviewableViewHolder>(AdapterUtil.diffCallback<Reviewable>()),
    Filterable {

    private var list: List<Reviewable> = listOf()

    fun submitData(data: List<Reviewable>, commitCallback: (() -> Unit) = {}) {
        list = data.toList()
        submitList(list, commitCallback)
    }

    inner class ReviewableViewHolder(reviewableView: View) :
        RecyclerView.ViewHolder(reviewableView) {

        private val reviewableTextView: TextView = reviewableView.findViewById(R.id.reviewableId)
        private var currentReviewable: Reviewable? = null

        init {
            reviewableView.isClickable = true
            reviewableView.findViewById<LinearLayout>(R.id.reviewableItemLayout)
                .setOnClickListener {
                    currentReviewable?.let {
                        onClick(it)
                    }
                }
        }

        /* Bind room id. */
        fun bind(reviewable: Reviewable) {
            currentReviewable = reviewable
            reviewableTextView.text = reviewable.toString()
        }
    }

    /* Creates and inflates view and return RoomViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewableViewHolder {
        return ReviewableViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reviewable_item,
                parent,
                false
            )
        )
    }

    /* Gets current room and uses it to bind view. */
    override fun onBindViewHolder(holder: ReviewableViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    override fun getFilter(): Filter {
        return reviewableSearchFilter
    }

    private fun getFilterMethod(filter: (CharSequence?) -> List<Reviewable>): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val results = FilterResults()

                if (query.isNullOrEmpty()) {
                    results.values = list
                    results.count = list.size
                } else {
                    val filteredList = filter(query)
                    results.values = filteredList
                    results.count = filteredList.size
                }

                return results
            }

            override fun publishResults(query: CharSequence?, searchResults: FilterResults?) {
                searchResults?.values
                    ?.run { submitList(this as List<Reviewable>) }
            }
        }
    }

    private val reviewableSearchFilter = getFilterMethod { query ->
        val queryLower = query.toString().lowercase()
        val filteredList = mutableListOf<Reviewable>()
        filteredList.addAll(list.filter { item ->
            item.toString()
                .lowercase()
                .contains(queryLower)
        })
        filteredList
    }

    /**
     * Sort the adapted list alphabetically, in the A-Z order by default
     *
     * @param reversedOrder: if true, sort following the Z-A order
     * @param commitCallback: callback to execute when the sort has been committed, default null
     */
    fun sortAlphabetically(reversedOrder: Boolean = false, commitCallback: (() -> Unit)? = {}) {
        val sortedList = list.sortedBy { item -> item.id }
            .let { if (reversedOrder) it.reversed() else it }

        submitList(sortedList, commitCallback)
    }

//    TODO("ADD BACK THOSE WHEN IMPLEMENTATION IS DONE"
//    fun filterByCredit(credit: CharSequence?) {
//        courseCreditsFilter.filter(credit)
//    }
//
//    private val courseCreditsFilter = getFilterMethod { query ->
//        val queryInt = query.toString()
//            .toInt()
//        val filteredList = mutableListOf<Reviewable>()
//        filteredList.addAll(list.filter { item ->
//            (item as Course).credits == queryInt
//        })
//        filteredList
//    }

}