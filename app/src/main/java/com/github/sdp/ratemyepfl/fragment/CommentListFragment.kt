package com.github.sdp.ratemyepfl.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.post.OnClickListener
import com.github.sdp.ratemyepfl.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.viewmodel.CommentListViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommentListFragment : Fragment(R.layout.fragment_comment_list) {

    private lateinit var commentAdapter: PostAdapter<Comment>
    private lateinit var recyclerView: RecyclerView
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
    private val viewModel by activityViewModels<CommentListViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    companion object {
        const val EXTRA_SUBJECT_COMMENTED_ID: String = "com.github.sdp.extra_subject_commented_id"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeReviewList(view)
        initializeProfilePanel(view)
    }

    private fun initializeReviewList(view: View) {
        commentAdapter = PostAdapter(
            viewLifecycleOwner, userViewModel,
            getListener { r, s -> viewModel.updateUpVotes(r, s) },
            getListener { r, s -> viewModel.updateDownVotes(r, s) },
            { cwa ->
                lifecycleScope.launch {
                    viewModel.removeComment(cwa.post.getId())
                }
            },
            { cwa -> displayProfilePanel(cwa.author, cwa.image) },
            R.layout.subject_item
        )

        recyclerView = view.findViewById(R.id.reviewRecyclerView)
        recyclerView.adapter = commentAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher = view.findViewById(R.id.reviewSwipeRefresh)
        swipeRefresher.setOnRefreshListener {
            viewModel.updateCommentsList()
            swipeRefresher.isRefreshing = false
        }

        viewModel.comments.observe(viewLifecycleOwner) {
            it?.let { commentAdapter.submitList(it) }
        }
    }

    private fun initializeProfilePanel(view: View) {
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
    }

    /**
     * Creates the onClickListener from the function given as input, encapsulating it in a
     * try catch to display the error message as SnackBar
     */
    private fun getListener(f: (Comment, String?) -> Unit) = OnClickListener<Comment> { cwa ->
        try {
            f(cwa.post, cwa.author?.uid)
        } catch (e: Exception) {
            /*e.message?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.reviewBottomNavigationView)
                    .show()
            }*/
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
        viewModel.updateCommentsList()
        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }
}