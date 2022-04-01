package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.google.common.collect.Ordering

class ReviewableAdapter(private val onClick: (Reviewable) -> Unit) :
    ListAdapter<Reviewable, ReviewableAdapter.ReviewableViewHolder>(ListActivityUtils.diffCallback<Reviewable>()),
    Filterable {

    private var list = mutableListOf<Reviewable>()

    inner class ReviewableViewHolder(reviewableView: View) :
        RecyclerView.ViewHolder(reviewableView) {

        private val reviewableTextView: TextView = reviewableView.findViewById(R.id.reviewableId)
        private var currentReviewable: Reviewable? = null

        init {
            reviewableView.isClickable = true
            reviewableView.findViewById<LinearLayout>(R.id.reviewableItemLayout).setOnClickListener {
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

    /**
     * Replace the current list content with [data]
     *
     * @param data: a list of items that are [Reviewable]
     *
     */
    fun setData(data: List<Reviewable>) {
        this.list = data.toMutableList()
        submitList(list)
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
                submitList(searchResults!!.values as MutableList<Reviewable>)
            }
        }
    }

    private val reviewableSearchFilter = getFilterMethod { query ->
        val queryLower = query.toString().lowercase()
        val filteredList = mutableListOf<Reviewable>()
        filteredList.addAll(list.filter {
            it.toString().lowercase().contains(queryLower)
        })
        filteredList
    }

    /**
     * Sort the adapted list alphabetically, in the A-Z order by default
     *
     * @param reversedOrder: if true, sort following the Z-A order (
     */
    fun sortAlphabetically(reversedOrder: Boolean = false) {
        val sortedList: MutableList<Reviewable> = list.toMutableList()
        sortedList.sortBy{ item -> item.id}
        if(reversedOrder) {
            sortedList.reverse()
        }
        setData(sortedList)
    }

    fun filterByCredit(credit: CharSequence?) {
        courseCreditsFilter.filter(credit)
    }

    private val courseCreditsFilter = getFilterMethod { query ->
        val queryInt = query.toString().toInt()
        val filteredList = mutableListOf<Reviewable>()
        filteredList.addAll(list.filter {
            (it as Course).credits == queryInt
        })
        filteredList
    }

}