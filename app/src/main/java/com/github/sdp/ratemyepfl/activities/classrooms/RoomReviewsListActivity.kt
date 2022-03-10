package com.github.sdp.ratemyepfl.activities.classrooms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapters.RoomReviewsAdapter
import com.github.sdp.ratemyepfl.placeholder.DataSource
import com.github.sdp.ratemyepfl.review.ClassroomReview
import com.github.sdp.ratemyepfl.viewmodels.RoomReviewsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomReviewsListActivity : AppCompatActivity() {

    @Inject
    lateinit var dataSource: DataSource
    lateinit var roomReviewsViewModel: RoomReviewsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_reviews_list)

        var currentRoomId: String? = null
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentRoomId = bundle.getString(ROOM_ID)
        }
        roomReviewsViewModel = RoomReviewsListViewModel(dataSource, currentRoomId)

        /*roomReviewsViewModel = ViewModelProvider(
            this,
            RoomReviewsListViewModel.RoomReviewsListViewModelFactory(DataSource(), currentRoomId)
        ).get(RoomReviewsListViewModel::class.java)*/

        val reviewsAdapter = RoomReviewsAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.review_recycler_view)
        recyclerView.adapter = reviewsAdapter

        // Display the reviews of the classroom
        currentRoomId?.let {
            roomReviewsViewModel.getReviews().observe(this) {
                it?.let {
                    reviewsAdapter.submitList(it as MutableList<ClassroomReview>)
                }
            }
        }

        // Floating action button for adding reviews
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabOnClick()
        }

    }


    /* Adds review */
    private fun fabOnClick() {
        resultLauncher.launch(Intent(this, AddRoomReviewActivity::class.java))
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val d: Intent? = result.data
                val roomGrade = d?.getStringExtra(ROOM_GRADE)
                val roomComment = d?.getStringExtra(ROOM_COMMENT)
                if (roomGrade != null && roomComment != null) {
                    roomReviewsViewModel.insertReview(roomGrade, roomComment)
                }
            }
        }
}