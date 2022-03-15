package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.FakeCoursesDatabase

class CoursesActivity : AppCompatActivity() {

    private lateinit var coursesView: ListView
    private lateinit var adapter: CoursesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        val fakeDatabase = FakeCoursesDatabase()
        val courseList = fakeDatabase.getCoursesList()

        coursesView = findViewById(R.id.coursesListView)
        adapter = CoursesAdapter(
            this, android.R.layout.simple_list_item_1,
            courseList
        )
        coursesView.adapter = adapter

        coursesView.isClickable = true
        coursesView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            displayCourseReviews(courseList[position].name)
        }
    }

    private fun displayCourseReviews(courseName: String) {
        val intent = Intent(this, ReviewsActivity::class.java)
        intent.putExtra(ReviewsActivity.EXTRA_COURSE_NAME, courseName)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.courses_options_menu, menu)

        val searchItem = menu!!.findItem(R.id.searchView)
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

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            adapter.sort { c1, c2 -> c1.name.compareTo(c2.name) }
            true
        }
        R.id.decreasingOrder -> {
            adapter.sort { c1, c2 -> c2.name.compareTo(c1.name) }
            true
        }
        R.id.credit_2, R.id.credit_3, R.id.credit_4, R.id.credit_5, R.id.credit_6, R.id.credit_7, R.id.credit_8 -> {
            adapter.filterByCredit(item.title)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}