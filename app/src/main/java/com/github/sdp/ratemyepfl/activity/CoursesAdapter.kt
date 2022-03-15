package com.github.sdp.ratemyepfl.activity

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import com.github.sdp.ratemyepfl.model.items.Course

class CoursesAdapter(
    context: Context,
    @LayoutRes private val resource: Int,
    private val allCourses: List<Course>
) :
    ArrayAdapter<Course>(context, resource, allCourses),
    Filterable {

    private var list: List<Course> = allCourses

    override fun getCount(): Int {
        return list.size
    }

    fun filterByCredit(credit: CharSequence?) {
        courseCreditsFilter.filter(credit)
    }

    private val courseCreditsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint.isNullOrEmpty()) {
                results.values = allCourses
                results.count = list.size
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
            list = results!!.values as List<Course>
            notifyDataSetChanged()
        }

    }

}
