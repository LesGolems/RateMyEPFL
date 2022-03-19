package com.github.sdp.ratemyepfl.activity.classrooms

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils.Companion.setUpSearchView
import com.github.sdp.ratemyepfl.viewmodel.ClassroomsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@AndroidEntryPoint
class ClassroomsListActivity : AppCompatActivity() {

    private lateinit var roomsAdapter: ReviewableAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ClassroomsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewable_list)

        roomsAdapter = ReviewableAdapter { room -> displayRoomReviews(room as Classroom) }
        recyclerView = findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = roomsAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        viewModel.getRooms().observe(this) {
            it?.let {
                roomsAdapter.setData(it as MutableList<Reviewable>)
            }
        }

    }

    /* Opens RoomReviewsActivity when RecyclerView item is clicked. */
    private fun displayRoomReviews(room: Classroom) {
        val intent = Intent(this, RoomReviewsListActivity::class.java)
        intent.putExtra(RoomReviewsListActivity.EXTRA_CLASSROOMS_JSON, Json.encodeToString(room))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rooms_options_menu, menu)
        setUpSearchView(menu, roomsAdapter, R.id.searchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            roomsAdapter.sortAlphabetically(true)
            true
        }
        R.id.decreasingOrder -> {
            roomsAdapter.sortAlphabetically(false)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}