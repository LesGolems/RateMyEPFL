package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.ui.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.ui.layout.LoadingView
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.github.sdp.ratemyepfl.viewmodel.review.ReviewListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Fragment for the list of reviews, shared among all reviewed items
 */
@AndroidEntryPoint
class ReviewListFragment : Fragment(R.layout.fragment_review_list) {
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var loadingRecyclerView: LoadingView<RecyclerView>
    private lateinit var swipeRefresher: SwipeRefreshLayout
    private lateinit var profilePanel: SlidingUpPanelLayout
    private lateinit var authorPanelImage: CircleImageView
    private lateinit var authorPanelUsername: TextView
    private lateinit var authorPanelEmail: TextView
    private lateinit var authorPanelEmailIcon: ImageView
    private lateinit var karmaCount: TextView

    @Inject
    lateinit var connectedUser: ConnectedUser

    // Gets the shared view model
    private val reviewsViewModel by activityViewModels<ReviewListViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresher = view.findViewById(R.id.reviewSwipeRefresh)
        val recyclerView: RecyclerView = view.findViewById(R.id.reviewRecyclerView)
        val progressBarRecyclerView: ProgressBar = view.findViewById(R.id.reviewListProgressBar)
        loadingRecyclerView = LoadingView(recyclerView, progressBarRecyclerView)
        profilePanel = view.findViewById(R.id.author_profile_panel)
        authorPanelImage = view.findViewById(R.id.author_panel_profile_image)
        authorPanelUsername = view.findViewById(R.id.author_panel_username)
        authorPanelEmail = view.findViewById(R.id.author_panel_email)
        authorPanelEmailIcon = view.findViewById(R.id.author_panel_email_icon)
        karmaCount = view.findViewById(R.id.karmaCount)

        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN

        profilePanel.setFadeOnClickListener {
            profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        }

        swipeRefresher.setOnRefreshListener {
            updateReviewsList()
            swipeRefresher.isRefreshing = false
        }

        setUpAdapter()

        val button = view.findViewById<ExtendedFloatingActionButton>(R.id.addReviewButton)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.addReviewFragment)
        }
    }

    private fun setUpAdapter() {
        reviewAdapter = ReviewAdapter(viewLifecycleOwner, userViewModel,
            getListener { r, s ->
                reviewsViewModel.updateUpVotes(r, s)
                updateReviewsList()
            },
            getListener { r, s ->
                reviewsViewModel.updateDownVotes(r, s)
                updateReviewsList()
            },
            { rwa ->
                lifecycleScope.launch {
                    reviewsViewModel.removeReview(rwa.review.getId())
                    updateReviewsList()
                }
            }
        ) { rwa -> displayProfilePanel(rwa.author, rwa.image) }

        loadingRecyclerView.view.adapter = reviewAdapter

        reviewsViewModel.reviews.observe(viewLifecycleOwner) {
            it?.let { reviewAdapter.submitList(it) }
        }
    }

    /**
     * Creates the onClickListener from the function given as input, encapsulating it in a
     * try catch to display the error message as SnackBar
     */
    private fun getListener(f: (Review, String?) -> Unit) = ReviewAdapter.OnClickListener { rwa ->
        try {
            f(rwa.review, rwa.author?.uid)
        } catch (e: Exception) {
            e.message?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun displayProfilePanel(author: User?, image: ImageFile?) {
        if (author != null) {
            authorPanelUsername.text = author.username
            authorPanelEmail.text = author.email
            karmaCount.text = author.karma.toString()

            if (image != null) {
                authorPanelImage.setImageBitmap(image.data)
            }
            profilePanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
    }

    override fun onResume() {
        super.onResume()
        updateReviewsList()
        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private fun updateReviewsList() {
        reviewsViewModel.viewModelScope
            .launch {
                loadingRecyclerView.display(reviewsViewModel.getReviews(), {
                    reviewsViewModel.reviews
                        .postValue(it)
                })
            }
    }
}