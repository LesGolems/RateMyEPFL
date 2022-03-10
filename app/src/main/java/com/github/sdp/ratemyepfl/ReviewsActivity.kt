package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.items.FakeCoursesDatabase

class ReviewsActivity : AppCompatActivity() {
    private lateinit var reviewsView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        val fakeDatabase = FakeCoursesDatabase()
        val courseList = fakeDatabase.getCoursesList()

        reviewsView = findViewById(R.id.coursesListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2,
            courseList)
        reviewsView.adapter = adapter
        reviewsView.isClickable = true
        reviewsView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            printCourse(courseList[position].name)
        }
    }

    private fun printCourse(courseName: String) {
        val intent = Intent(this, GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, courseName)
        startActivity(intent)
    }
}