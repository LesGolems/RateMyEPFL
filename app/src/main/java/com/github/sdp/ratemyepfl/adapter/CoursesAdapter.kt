package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course


class CoursesAdapter(private val onClick: (Course) -> Unit) :
    ListAdapter<Course, CoursesAdapter.CourseViewHolder>(CourseDiffCallback),
    Filterable {

    private var list = mutableListOf<Course>()

    inner class CourseViewHolder(courseView: View) :
        RecyclerView.ViewHolder(courseView) {

        private val courseTextView: TextView = courseView.findViewById(R.id.courseId)
        private var currentCourse: Course? = null

        init {
            courseView.setOnClickListener {
                currentCourse?.let {
                    onClick(it)
                }
            }
        }

        /* Bind course name. */
        fun bind(course: Course) {
            currentCourse = course
            courseTextView.text = course.name
        }
    }

    /* Creates and inflates view and return CourseViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.course_item,
                parent,
                false
            )
        )
    }

    /* Gets current course and uses it to bind view. */
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    fun setData(list: MutableList<Course>?) {
        this.list = list!!
        submitList(list)
    }

    override fun getFilter(): Filter {
        return courseSearchFilter
    }

    private val courseSearchFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val results = FilterResults()

            if (query.isNullOrEmpty()) {
                results.values = list
                results.count = list.size
            } else {
                val queryLower = query.toString().lowercase()
                val filteredList = mutableListOf<Course>()
                filteredList.addAll(list.filter {
                    it.name.lowercase().contains(queryLower)
                })
                results.values = filteredList
                results.count = filteredList.size
            }

            return results
        }

        override fun publishResults(query: CharSequence?, searchResults: FilterResults?) {
            submitList(searchResults!!.values as MutableList<Course>)
        }
    }

    fun sortAlphabetically(increasing: Boolean) {
        val sortedList = mutableListOf<Course>()
        sortedList.addAll(list)

        sortedList.sortBy { it.name }
        if (!increasing) {
            sortedList.reverse()
        }
        setData(sortedList)
    }

    fun filterByCredit(credit: CharSequence?) {
        courseCreditsFilter.filter(credit)
    }

    private val courseCreditsFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val results = FilterResults()

            if (query.isNullOrEmpty()) {
                results.values = list
                results.count = list.size
            } else {
                val queryInt = query.toString().toInt()
                val filteredList = mutableListOf<Course>()
                filteredList.addAll(list.filter {
                    it.credits == queryInt
                })
                results.values = filteredList
                results.count = filteredList.size
            }

            return results
        }

        override fun publishResults(query: CharSequence?, results: FilterResults?) {
            submitList(results!!.values as MutableList<Course>)
        }

    }

}

object CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.courseCode == newItem.courseCode
    }
}
