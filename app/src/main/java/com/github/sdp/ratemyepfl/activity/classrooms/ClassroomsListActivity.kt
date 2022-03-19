package com.github.sdp.ratemyepfl.activity.classrooms

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.ClassroomsListViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.getRooms().observe(this) {
            it?.let {
                roomsAdapter.setData(it as MutableList<Reviewable>)
            }
        }

    }

    /* Opens RoomReviewsActivity when RecyclerView item is clicked. */
    private fun displayRoomReviews(room: Classroom) {
        val intent = Intent(this, RoomReviewsListActivity()::class.java)
        intent.putExtra(EXTRA_ROOM_ID, room.id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rooms_options_menu, menu)

        val searchItem = menu!!.findItem(R.id.searchView)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                roomsAdapter.filter.filter(newText)
                return true
            }
        })

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

    companion object {
        const val EXTRA_ROOM_ID = "com.github.sdp.ratemyepfl.activity.classrooms.extra_room_id"
    }
}