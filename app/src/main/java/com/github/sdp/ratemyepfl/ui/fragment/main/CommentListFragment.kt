package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.review.ObjectWithAuthor
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
    R.layout.comment_item
) {

    private lateinit var commentPanel: SlidingUpPanelLayout
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
        noPostTextView.text = getString(R.string.empty_post_list_message, "comments")
        initializeAddReview(view)
        setupListeners()
    }

    private fun initializeAddReview(view: View) {
        commentPanel = view.findViewById(R.id.commentPanel)
        commentPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        commentPanel.setFadeOnClickListener {
            if (commentPanel.panelState == SlidingUpPanelLayout.PanelState.EXPANDED)
                commentPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        doneButton = view.findViewById(R.id.doneButton)
        comment = view.findViewById(R.id.addComment)
        anonymousSwitch = view.findViewById(R.id.addCommentAnonymousSwitch)
    }

    private fun setupListeners() {
        // Expands the panel when the user wants to comment
        comment.setOnClickListener {
            if (commentPanel.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                commentPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            else
                commentPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
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
            commentPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
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
        commentPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    override fun posts(): MutableLiveData<List<ObjectWithAuthor<Comment>>> {
        return viewModel.comments
    }

    override fun isEmpty(): LiveData<Boolean> {
        return viewModel.isEmpty
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