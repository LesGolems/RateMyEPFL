package com.github.sdp.ratemyepfl.activity.classrooms

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils.Companion.setUpSearchView
import com.github.sdp.ratemyepfl.viewmodel.ClassroomsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class ClassroomsListActivity : ReviewableListActivity<Classroom>() {

    private val viewModel: ClassroomsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRooms().observe(this) {
            it?.let {
                reviewableAdapter.setData(it as MutableList<Reviewable>)
            }
        }
    }

    override fun displayReviews(t: Classroom) {
        val intent = Intent(this, RoomReviewsListActivity::class.java)
        intent.putExtra(RoomReviewsListActivity.EXTRA_CLASSROOMS_JSON, Json.encodeToString(t))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rooms_options_menu, menu)
        setUpSearchView(menu, reviewableAdapter, R.id.roomSearchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            reviewableAdapter.sortAlphabetically(true)
            true
        }
        R.id.decreasingOrder -> {
            reviewableAdapter.sortAlphabetically(false)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}