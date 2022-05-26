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
import com.github.sdp.ratemyepfl.ui.layout.LoadingRecyclerView
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Fragment for a list of posts (Review, Subject, Comment)
 */
abstract class PostListFragment<T : Post>(
    fragmentLayout: Int,
    private val recyclerViewLayout: Int,
    private val swipeRefreshLayout: Int,
    private val adapterLayout: Int
) : Fragment(fragmentLayout) {

    lateinit var postAdapter: PostAdapter<T>
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresher: SwipeRefreshLayout
    lateinit var loadingRecyclerView: LoadingRecyclerView

    lateinit var profilePanel: SlidingUpPanelLayout
    lateinit var authorPanelImage: CircleImageView
    lateinit var authorPanelUsername: TextView
    lateinit var authorPanelEmail: TextView
    lateinit var authorPanelEmailIcon: ImageView
    lateinit var karmaCount: TextView

    @Inject
    lateinit var connectedUser: ConnectedUser

    protected val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePostList(view)
        initializeProfilePanel(view)
    }

    abstract fun posts(): MutableLiveData<List<PostWithAuthor<T>>>
    abstract fun updatePostsList()
    abstract fun updateUpVotes(post: T, uid: String?)
    abstract fun updateDownVotes(post: T, uid: String?)
    abstract fun removePost(postId: String)

    suspend fun displayPosts(posts: Flow<List<PostWithAuthor<T>>>) {
        loadingRecyclerView.display(posts, {
            posts().postValue(it)
        })
    }

    open fun initializePostList(view: View) {

        val listLayout: View = view.findViewById(recyclerViewLayout)
        postAdapter = setupAdapter(view)
        loadingRecyclerView = LoadingRecyclerView(listLayout)

        loadingRecyclerView.recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher = view.findViewById(swipeRefreshLayout)
        swipeRefresher.setOnRefreshListener {
            updatePostsList()
            swipeRefresher.isRefreshing = false
        }

        posts().observe(viewLifecycleOwner) {
            it?.let { postAdapter.submitList(it) }
        }
    }

    open fun setupAdapter(view: View): PostAdapter<T> =
        PostAdapter(
            viewLifecycleOwner, userViewModel,
            getListener({ post, uid -> updateUpVotes(post, uid) }, view),
            getListener({ post, uid -> updateDownVotes(post, uid) }, view),
            { postWithAuthor -> removePost(postWithAuthor.post.getId()) },
            { postWithAuthor -> displayProfilePanel(postWithAuthor.author, postWithAuthor.image) },
            adapterLayout
        )

    open fun initializeProfilePanel(view: View) {
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

    open fun displayProfilePanel(author: User?, image: ImageFile?) {
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
        updatePostsList()
        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }
}