package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.ui.fragment.PostListFragment
import com.github.sdp.ratemyepfl.viewmodel.review.ReviewListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment for the list of reviews, shared among all reviewed items
 */
@AndroidEntryPoint
class ReviewListFragment : PostListFragment<Review>(
    R.layout.fragment_review_list,
    R.id.reviewRecyclerView,
    R.id.reviewSwipeRefresh,
    R.layout.review_item
) {

    private lateinit var addReviewButton: FloatingActionButton

    // Gets the shared view model
    private val reviewsViewModel by activityViewModels<ReviewListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addReviewButton = view.findViewById(R.id.addReviewButton)
        addReviewButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.addReviewFragment)
        }
    }

    override fun posts(): MutableLiveData<List<PostWithAuthor<Review>>> {
        return reviewsViewModel.reviews
    }

    override fun updatePostsList() {
        reviewsViewModel.updateReviewsList()
    }

    override fun updateUpVotes(post: Review, uid: String?) {
        reviewsViewModel.updateUpVotes(post, uid)
    }

    override fun updateDownVotes(post: Review, uid: String?) {
        reviewsViewModel.updateDownVotes(post, uid)
    }

    override fun removePost(postId: String) {
        reviewsViewModel.removeReview(postId)
    }
}