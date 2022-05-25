package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.ui.fragment.AddPostFragment
import com.github.sdp.ratemyepfl.ui.fragment.PostListFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnToast
import com.github.sdp.ratemyepfl.viewmodel.main.CommentListViewModel
import com.google.android.material.textfield.TextInputEditText
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentListFragment : PostListFragment<Comment>(
    R.layout.fragment_comment_list,
    R.id.commentRecyclerView,
    R.id.commentSwipeRefresh,
    R.layout.comment_item
) {

    private lateinit var slidingLayout: SlidingUpPanelLayout
    private lateinit var comment: TextInputEditText
    private lateinit var anonymousSwitch: SwitchCompat
    private lateinit var doneButton: Button

    // Gets the shared view model
    private val viewModel by activityViewModels<CommentListViewModel>()

    companion object {
        const val EXTRA_SUBJECT_COMMENTED_ID: String = "com.github.sdp.extra_subject_commented_id"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.id = arguments?.getString(EXTRA_SUBJECT_COMMENTED_ID)!!
        initializeAddReview(view)
        setupListeners()
    }

    private fun initializeAddReview(view: View) {
        slidingLayout = view.findViewById(R.id.slidingAddComment)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        slidingLayout.setFadeOnClickListener {
            if (slidingLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED)
                slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
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

        comment.addTextChangedListener(AddPostFragment.onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setComment(text?.toString())
        })

        anonymousSwitch.setOnCheckedChangeListener { _, b ->
            viewModel.setAnonymous(b)
        }

        doneButton.setOnClickListener {
            addComment()
        }
    }

    private fun addComment() {
        val context = requireContext()
        try {
            viewModel.submitComment()
            comment.setText("")
            displayOnToast(context, getString(R.string.comment_sent_text))
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
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    override fun posts(): MutableLiveData<List<PostWithAuthor<Comment>>> {
        return viewModel.comments
    }

    override fun updatePostsList() {
        viewModel.updateCommentsList()
    }

    override fun updateUpVotes(post: Comment, uid: String?) {
        viewModel.updateUpVotes(post, uid)
    }

    override fun updateDownVotes(post: Comment, uid: String?) {
        viewModel.updateDownVotes(post, uid)
    }

    override fun removePost(postId: String) {
        viewModel.removeComment(postId)
    }
}