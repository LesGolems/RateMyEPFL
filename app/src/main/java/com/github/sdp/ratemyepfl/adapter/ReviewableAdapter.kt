package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils

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
            reviewableTextView.setOnClickListener {
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

    fun setData(list: MutableList<Reviewable>?) {
        this.list = list!!
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

    fun sortAlphabetically(increasing: Boolean) {
        val sortedList = mutableListOf<Reviewable>()
        sortedList.addAll(list)

        sortedList.sortBy { it.id }
        if (!increasing) {
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