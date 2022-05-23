package com.github.sdp.ratemyepfl.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Post
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

abstract class PostListFragment<T : Post>(
    val layout: Int,
    /*private val recyclerViewLayout: Int,
    private val swipeRefreshLayout: Int,
    private val adapterLayout: Int,*/
) : Fragment(layout) {

    /*private lateinit var postAdapter: PostAdapter<T>
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout*/

    private lateinit var profilePanel: SlidingUpPanelLayout
    private lateinit var authorPanelImage: CircleImageView
    private lateinit var authorPanelUsername: TextView
    private lateinit var authorPanelEmail: TextView
    private lateinit var authorPanelEmailIcon: ImageView
    private lateinit var karmaCount: TextView

    /*@Inject
    lateinit var connectedUser: ConnectedUser

    private val userViewModel by activityViewModels<UserViewModel>()*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializeReviewList(view)
        initializeProfilePanel(view)
    }

    /*abstract fun posts(): MutableLiveData<List<PostWithAuthor<T>>>
    abstract fun updatePostsList()
    abstract fun updateUpVotes(post: T, uid: String?)
    abstract fun updateDownVotes(post: T, uid: String?)
    abstract fun removePost(postId: String)

    private fun initializeReviewList(view: View) {
        recyclerView = view.findViewById(recyclerViewLayout)
        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher = view.findViewById(swipeRefreshLayout)
        swipeRefresher.setOnRefreshListener {
            updatePostsList()
            swipeRefresher.isRefreshing = false
        }

        setUpAdapter()
    }

    private fun setUpAdapter() {
        val context = requireContext()
        postAdapter = PostAdapter(
            viewLifecycleOwner, userViewModel,
            getListener(
                { post, uid -> updateUpVotes(post, uid) },
                context
            ),
            getListener(
                { post, uid -> updateDownVotes(post, uid) },
                context
            ),
            { postWithAuthor ->
                removePost(postWithAuthor.post.getId())

            },
            { rwa -> displayProfilePanel(rwa.author, rwa.image) },
            adapterLayout
        )

        recyclerView.adapter = postAdapter

        posts().observe(viewLifecycleOwner) {
            it?.let { postAdapter.submitList(it) }
        }
    }*/

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

    protected fun displayProfilePanel(author: User?, image: ImageFile?) {
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
        //updatePostsList()
        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }
}