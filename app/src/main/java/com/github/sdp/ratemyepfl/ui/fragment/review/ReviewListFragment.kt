package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.post.ObjectWithAuthor
import com.github.sdp.ratemyepfl.model.post.Review
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.ui.adapter.post.ReviewAdapter
import com.github.sdp.ratemyepfl.ui.fragment.PostListFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.review.ReviewListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/*
Fragment for the list of reviews, shared among all reviewed items
 */
@AndroidEntryPoint
class ReviewListFragment : PostListFragment<Review>(
    R.layout.fragment_review_list,
    R.layout.review_item,
    R.id.reviewRecyclerView
) {

    private lateinit var emptyListMessage: String

    // Gets the shared view model
    private val viewModel by activityViewModels<ReviewListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<ExtendedFloatingActionButton>(R.id.addReviewButton)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.addReviewFragment)
        }

        emptyListMessage = getString(R.string.empty_post_list_message, "reviews")
    }

    override fun setupAdapter(view: View): PostAdapter<Review> =
        ReviewAdapter(
            viewLifecycleOwner, userViewModel,
            getListener({ review, uid -> updateUpVotes(review, uid) }, view),
            getListener({ review, uid -> updateDownVotes(review, uid) }, view),
            { rwa -> removePost(rwa.obj.getId()) },
            { rwa -> displayProfilePanel(rwa.author, rwa.image) }
        )

    override fun posts(): MutableLiveData<List<ObjectWithAuthor<Review>>> {
        return viewModel.reviews
    }

    override fun updatePostsList() {
        viewModel.viewModelScope
            .launch {
                displayPosts(viewModel.getReviews(), emptyListMessage)
            }
    }

    override fun updateUpVotes(post: Review, uid: String?) {
        viewModel.updateUpVotes(post, uid)
    }

    override fun updateDownVotes(post: Review, uid: String?) {
        viewModel.updateDownVotes(post, uid)
    }

    override fun onResume() {
        super.onResume()
        updatePostsList()
        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    override fun removePost(postId: String) {
        viewModel.removeReview(postId)
        updatePostsList()
    }

}