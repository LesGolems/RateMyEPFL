package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.post.OnClickListener
import com.github.sdp.ratemyepfl.adapter.post.SubjectAdapter
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.fragment.CommentListFragment
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.viewmodel.HomeViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var userTop1Picture: CircleImageView
    private lateinit var userTop2Picture: CircleImageView
    private lateinit var userTop3Picture: CircleImageView

    private lateinit var subjectAdapter: SubjectAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout

    private lateinit var profilePanel: SlidingUpPanelLayout
    private lateinit var authorPanelImage: CircleImageView
    private lateinit var authorPanelUsername: TextView
    private lateinit var authorPanelEmail: TextView
    private lateinit var authorPanelEmailIcon: ImageView
    private lateinit var karmaCount: TextView

    private lateinit var userProfilePicture: CircleImageView
    private lateinit var subjectInputText: EditText

    @Inject
    lateinit var connectedUser: ConnectedUser

    private val viewModel by activityViewModels<HomeViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePodium(view)
        initializePersonalTab(view)
        initializeReviewList(view)
        initializeProfilePanel(view)
    }

    private fun initializePodium(view: View) {
        userTop1Picture = view.findViewById(R.id.userTop1Picture)
        userTop2Picture = view.findViewById(R.id.userTop2Picture)
        userTop3Picture = view.findViewById(R.id.userTop3Picture)

        Log.d("TAG", viewModel.topUsers.value.toString())
        viewModel.topUsersPictures.observe(viewLifecycleOwner) {
            it?.let {
                userTop1Picture.setImageBitmap(it[0]?.data)
                userTop2Picture.setImageBitmap(it[1]?.data)
                userTop3Picture.setImageBitmap(it[2]?.data)
            }
        }
    }

    private fun initializePersonalTab(view: View) {
        subjectInputText = view.findViewById(R.id.subjectInputText)
        subjectInputText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.addSubjectFragment)
        }

        userProfilePicture = view.findViewById(R.id.userProfilePicture)
        userViewModel.picture.observe(viewLifecycleOwner) {
            it?.let { userProfilePicture.setImageBitmap(it.data) }
        }
        userProfilePicture.setOnClickListener {
            // TODO open side bar
        }
    }

    private fun initializeReviewList(view: View) {
        subjectAdapter = SubjectAdapter(viewLifecycleOwner, userViewModel,
            getListener { r, s -> viewModel.updateUpVotes(r, s) },
            getListener { r, s -> viewModel.updateDownVotes(r, s) },
            { swa ->
                lifecycleScope.launch { viewModel.removeSubject(swa.post.postId) }
            },
            { swa ->
                val bundle =
                    bundleOf(CommentListFragment.EXTRA_SUBJECT_COMMENTED_ID to swa.post.postId)
                Navigation.findNavController(view).navigate(R.id.commentListFragment, bundle)
            }
        ) { swa -> displayProfilePanel(swa.author, swa.image) }

        recyclerView = view.findViewById(R.id.subjectRecyclerView)
        recyclerView.adapter = subjectAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher = view.findViewById(R.id.subjectSwipeRefresh)
        swipeRefresher.setOnRefreshListener {
            viewModel.updateSubjectsList()
            viewModel.updatePodium()
            swipeRefresher.isRefreshing = false
        }

        viewModel.subjects.observe(viewLifecycleOwner) {
            it?.let { subjectAdapter.submitList(it) }
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
    private fun getListener(f: (Subject, String?) -> Unit) = OnClickListener<Subject> { swa ->
        try {
            f(swa.post, swa.author?.uid)
        } catch (e: Exception) {
            e.message?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.activityMainBottomNavigationView)
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
        viewModel.updateSubjectsList()
        profilePanel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }
}