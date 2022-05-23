package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.ui.adapter.post.ReviewAdapter
import com.github.sdp.ratemyepfl.ui.fragment.PostListFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.github.sdp.ratemyepfl.viewmodel.review.ReviewListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
Fragment for the list of reviews, shared among all reviewed items
 */
@AndroidEntryPoint
class ReviewListFragment : PostListFragment<Review>(R.layout.fragment_review_list) {

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout

    private lateinit var addReviewButton: FloatingActionButton

    @Inject
    lateinit var connectedUser: ConnectedUser

    // Gets the shared view model
    private val reviewsViewModel by activityViewModels<ReviewListViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeReviewList(view)

        addReviewButton = view.findViewById(R.id.addReviewButton)
        addReviewButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.addReviewFragment)
        }
    }

    private fun initializeReviewList(view: View) {
        recyclerView = view.findViewById(R.id.reviewRecyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )
        setupAdapter()

        swipeRefresher = view.findViewById(R.id.reviewSwipeRefresh)
        swipeRefresher.setOnRefreshListener {
            reviewsViewModel.updateReviewsList()
            swipeRefresher.isRefreshing = false
        }

    }

    private fun setupAdapter() {
        val context = requireContext()
        reviewAdapter = ReviewAdapter(viewLifecycleOwner, userViewModel,
            getListener({ r, s -> reviewsViewModel.updateUpVotes(r, s) }, context),
            getListener({ r, s -> reviewsViewModel.updateDownVotes(r, s) }, context),
            { rwa -> reviewsViewModel.removeReview(rwa.post.getId()) },
            { rwa -> displayProfilePanel(rwa.author, rwa.image) })

        recyclerView.adapter = reviewAdapter

        reviewsViewModel.reviews.observe(viewLifecycleOwner) {
            it?.let { reviewAdapter.submitList(it) }
        }
    }


    override fun onResume() {
        super.onResume()
        reviewsViewModel.updateReviewsList()
    }
}