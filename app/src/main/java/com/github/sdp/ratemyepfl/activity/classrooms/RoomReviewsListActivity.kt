package com.github.sdp.ratemyepfl.activity.classrooms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.RoomReviewsAdapter
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewsListActivity : AppCompatActivity() {

    private val viewModel: RoomReviewsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_reviews_list)

        val reviewsAdapter = RoomReviewsAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.review_recycler_view)
        recyclerView.adapter = reviewsAdapter

        // Display the reviews of the classroom
        viewModel.id.let {
            viewModel.getReviews().observe(this) {
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
        val intent = Intent(this, AddRoomReviewActivity::class.java)
        intent.putExtra(AddRoomReviewActivity.EXTRA_ROOM_REVIEWED, viewModel.id)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val d: Intent? = result.data
                val roomGrade: Int? = d?.getIntExtra(ROOM_GRADE, INVALID_ROOM_GRADE)
                val roomComment = d?.getStringExtra(ROOM_COMMENT)
                if (roomGrade != null && roomGrade != INVALID_ROOM_GRADE && roomComment != null) {
                    viewModel.insertReview(roomGrade, roomComment)
                }
            }
        }

    companion object {
        const val INVALID_ROOM_GRADE: Int = -1
    }
}