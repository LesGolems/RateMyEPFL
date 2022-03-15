package com.github.sdp.ratemyepfl.activity.classrooms

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ClassroomsAdapter
import com.github.sdp.ratemyepfl.viewmodel.ClassroomsListViewModel
import java.util.*

const val ROOM_ID = "room id"

class ClassroomsListActivity : AppCompatActivity() {

    private lateinit var viewModel: ClassroomsListViewModel
    private lateinit var roomsAdapter: ClassroomsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classrooms_list)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ClassroomsListViewModel::class.java)

        roomsAdapter = ClassroomsAdapter { room -> adapterOnClick(room) }
        recyclerView = findViewById(R.id.rooms_recycler_view)
        recyclerView.adapter = roomsAdapter

        viewModel.getRooms().observe(this) {
            it?.let {
                roomsAdapter.setData(it as MutableList<Classroom>)
            }
        }

    }

    /* Opens RoomReviewsActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(room: Classroom) {
        val intent = Intent(this, RoomReviewsListActivity()::class.java)
        intent.putExtra(ROOM_ID, room.id)
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
                roomsAdapter.filter.filter(newText)
                return true
            }
        })

        val sortView = menu.findItem(R.id.settingsView)
        sortView.setOnMenuItemClickListener {
            roomsAdapter.sortAlphabetically()
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.settingsView -> {
            roomsAdapter.sortAlphabetically()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }*/

}