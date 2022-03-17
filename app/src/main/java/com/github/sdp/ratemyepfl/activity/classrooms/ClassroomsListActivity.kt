package com.github.sdp.ratemyepfl.activity.classrooms

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ClassroomsAdapter
import com.github.sdp.ratemyepfl.viewmodel.ClassroomsListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClassroomsListActivity : AppCompatActivity() {

    private val viewModel: ClassroomsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classrooms_list)

        val roomsAdapter = ClassroomsAdapter { room -> adapterOnClick(room) }
        val recyclerView: RecyclerView = findViewById(R.id.rooms_recycler_view)
        recyclerView.adapter = roomsAdapter

        viewModel.getRooms().observe(this) {
            // update UI
            it?.let {
                roomsAdapter.submitList(it as MutableList<Classroom>)
            }
        }

    }

    /* Opens RoomReviewsActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(room: Classroom) {
        val intent = Intent(this, RoomReviewsListActivity()::class.java)
        intent.putExtra(EXTRA_ROOM_ID, room.id)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_ROOM_ID = "com.github.sdp.ratemyepfl.activity.classrooms.extra_room_id"
    }
}