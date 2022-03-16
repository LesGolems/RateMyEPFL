package com.github.sdp.ratemyepfl.activity

import android.content.Context
import android.database.DataSetObserver
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import com.github.sdp.ratemyepfl.model.items.Course

class CoursesAdapter(
    context: Context,
    @LayoutRes private val resource: Int,
    private val allCourses: List<Course>
) :
    ArrayAdapter<Course>(context, resource, allCourses) {

    private var list = mutableListOf<Course>()

    init {
        list.addAll(allCourses)
        //notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        super.registerDataSetObserver(observer)
    }

    override fun getFilter(): Filter {
        return courseSearchFilter
    }

    fun filterByName(name: CharSequence?) {
        courseSearchFilter.filter(name)
    }

    private val courseSearchFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val results = FilterResults()

            if (query.isNullOrEmpty()) {
                results.values = allCourses
                results.count = allCourses.size
            } else {
                val queryLower = query.toString().lowercase()
                val filteredList = allCourses.filter {
                    it.name.lowercase().contains(queryLower)
                }
                results.values = filteredList
                results.count = filteredList.size
            }

            return results
        }

        override fun publishResults(query: CharSequence?, searchResults: FilterResults?) {
            val filtered = searchResults!!.values as List<Course>
            list.clear()
            list.addAll(filtered)

            notifyDataSetChanged()
        }
    }

    fun filterByCredit(credit: CharSequence?) {
        courseCreditsFilter.filter(credit)
    }

    private val courseCreditsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint.isNullOrEmpty()) {
                results.values = allCourses
                results.count = allCourses.size
            } else {
                val queryInt = constraint.toString().toInt()
                val filteredList = allCourses.filter {
                    it.credits == queryInt
                }
                results.values = filteredList
                results.count = filteredList.size
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filtered = results!!.values as List<Course>
            list.clear()
            list.addAll(filtered)

            notifyDataSetChanged()
        }

    }
}
