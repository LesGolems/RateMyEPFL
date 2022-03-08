package com.github.sdp.ratemyepfl.review

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.Classroom
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapters.ClassroomsAdapter
import com.github.sdp.ratemyepfl.viewmodels.ClassroomsListViewModel

const val ROOM_ID = "room id"

class ClassroomsListActivity : AppCompatActivity() {

    private lateinit var viewModel: ClassroomsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classrooms_list)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ClassroomsListViewModel::class.java)

        val roomsAdapter = ClassroomsAdapter { room -> adapterOnClick(room) }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = roomsAdapter

        viewModel.getRooms().observe(this) {
            // update UI
            it?.let {
                roomsAdapter.submitList(it as MutableList<Classroom>)
            }
        }

    }

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(room: Classroom) {
        val intent = Intent(this, RoomReviewsListActivity()::class.java)
        intent.putExtra(ROOM_ID, room.id)
        startActivity(intent)
    }



}