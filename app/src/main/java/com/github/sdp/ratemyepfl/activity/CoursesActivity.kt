package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.FakeCoursesDatabase

class CoursesActivity : AppCompatActivity() {

    private lateinit var coursesView: ListView
    private lateinit var adapter: ArrayAdapter<Course>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        val fakeDatabase = FakeCoursesDatabase()
        val courseList = fakeDatabase.getCoursesList()

        coursesView = findViewById(R.id.coursesListView)
        adapter = ArrayAdapter(
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
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu!!.findItem(R.id.searchView)
        val searchView = menuItem.actionView as SearchView
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

        val sortView = menu.findItem(R.id.settingsView)
        sortView.setOnMenuItemClickListener {
            adapter.sort { o1, o2 -> o1.name.compareTo(o2.name) }
            true
        }


        return super.onCreateOptionsMenu(menu)
    }
}