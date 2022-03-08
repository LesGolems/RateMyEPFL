package com.github.sdp.ratemyepfl.review

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapters.RoomReviewsAdapter
import com.github.sdp.ratemyepfl.placeholder.DataSource
import com.github.sdp.ratemyepfl.viewmodels.RoomReviewsListViewModel

class RoomReviewsListActivity : AppCompatActivity() {
    private lateinit var roomReviewsViewModel: RoomReviewsListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_reviews_list)

        var currentRoomId: String? = null
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentRoomId = bundle.getString(ROOM_ID)
        }

        roomReviewsViewModel = ViewModelProvider(
            this,
            RoomReviewsListViewModel.RoomReviewsListViewModelFactory(DataSource(), currentRoomId)
            //ViewModelProvider.NewInstanceFactory()
        ).get(RoomReviewsListViewModel::class.java)


        val reviewsAdapter = RoomReviewsAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.review_recycler_view)
        recyclerView.adapter = reviewsAdapter


        currentRoomId?.let {
            roomReviewsViewModel.getReviews().observe(this) {
                it?.let {
                    reviewsAdapter.submitList(it as MutableList<ClassroomReview>)
                }
            }
        }

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabOnClick()
        }


    }


    /* Adds flower to flowerList when FAB is clicked. */
    private fun fabOnClick() {
        resultLauncher.launch(Intent(this, AddRoomReviewActivity::class.java))
        //startActivityForResult(intent, newRoomReviewActivityRequestCode)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request code
                val d: Intent? = result.data
                val roomGrade = d?.getStringExtra(ROOM_GRADE)
                val roomComment = d?.getStringExtra(ROOM_COMMENT)
                if (roomGrade != null && roomComment != null) {
                    roomReviewsViewModel.insertReview(roomGrade, roomComment)
                } // else ...
            }

        }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /* Inserts flower into viewModel. */
        if (requestCode == newRoomReviewActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let { d ->
                val roomGrade = d.getStringExtra(ROOM_GRADE)
                val roomComment = d.getStringExtra(ROOM_COMMENT)
                if (roomGrade != null && roomComment != null) {
                    roomReviewsViewModel.insertReview(roomGrade, roomComment)
                } // else ...
            }
        }
    }*/
}