package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.ui.fragment.review.AddReviewFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnToast
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.main.CommentListViewModel
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentListFragment : Fragment(R.layout.fragment_comment_list) {

    private lateinit var commentAdapter: PostAdapter<Comment>
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout

    private lateinit var slidingLayout: SlidingUpPanelLayout
    private lateinit var comment: TextInputEditText
    private lateinit var anonymousSwitch: SwitchCompat
    private lateinit var doneButton: Button
    private lateinit var textLayout: TextInputLayout

    @Inject
    lateinit var connectedUser: ConnectedUser

    // Gets the shared view model
    private val viewModel by activityViewModels<CommentListViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    companion object {
        const val EXTRA_SUBJECT_COMMENTED_ID: String = "com.github.sdp.extra_subject_commented_id"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.id = arguments?.getString(EXTRA_SUBJECT_COMMENTED_ID)!!
        initializeCommentList(view)
        initializeAddReview(view)
        setupListeners()
    }

    private fun initializeCommentList(view: View) {
        recyclerView = view.findViewById(R.id.commentRecyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher = view.findViewById(R.id.commentSwipeRefresh)
        swipeRefresher.setOnRefreshListener {
            viewModel.updateCommentsList()
            swipeRefresher.isRefreshing = false
        }

        setupAdapter()
    }

    private fun initializeAddReview(view: View) {
        slidingLayout = view.findViewById(R.id.slidingAddComment)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED

        doneButton = view.findViewById(R.id.doneButton)
        comment = view.findViewById(R.id.addComment)
        anonymousSwitch = view.findViewById(R.id.addCommentAnonymousSwitch)
    }

    private fun setupListeners() {
        // Expands the panel when the user wants to comment
        comment.setOnClickListener {
            if (slidingLayout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            else
                slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

        comment.addTextChangedListener(AddReviewFragment.onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setComment(text?.toString())
        })

        anonymousSwitch.setOnCheckedChangeListener { _, b ->
            viewModel.setAnonymous(b)
        }

        doneButton.setOnClickListener {
            addComment()
        }
    }


    private fun setupAdapter() {
        val context = requireContext()
        commentAdapter = PostAdapter(
            viewLifecycleOwner, userViewModel,
            getListener({ r, s -> viewModel.updateUpVotes(r, s) }, context),
            getListener({ r, s -> viewModel.updateDownVotes(r, s) }, context),
            { cwa -> viewModel.removeComment(cwa.post.postId) },
            { cwa -> },//displayProfilePanel(cwa.author, cwa.image) },
            R.layout.comment_item
        )

        recyclerView.adapter = commentAdapter

        viewModel.comments.observe(viewLifecycleOwner) {
            it?.let { commentAdapter.submitList(it) }
        }
    }

    private fun addComment() {
        val context = requireContext()
        try {
            viewModel.submitComment()
            comment.setText("")
            displayOnToast(context, "Your post was submitted!")
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } catch (due: DisconnectedUserException) {
            displayOnToast(context, due.message)
        } catch (mie: MissingInputException) {
            if (comment.text.isNullOrEmpty()) {
                comment.error = mie.message
            } else {
                displayOnToast(context, mie.message)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateCommentsList()
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }
}