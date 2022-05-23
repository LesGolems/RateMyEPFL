package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.ui.adapter.post.OnClickListener
import com.github.sdp.ratemyepfl.ui.adapter.post.SubjectAdapter
import com.github.sdp.ratemyepfl.viewmodel.main.HomeViewModel
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var userTop1Picture: CircleImageView
    private lateinit var userTop1Name: TextView
    private lateinit var userTop1Karma: TextView

    private lateinit var userTop2Picture: CircleImageView
    private lateinit var userTop2Name: TextView
    private lateinit var userTop2Karma: TextView

    private lateinit var userTop3Picture: CircleImageView
    private lateinit var userTop3Name: TextView
    private lateinit var userTop3Karma: TextView

    private lateinit var subjectAdapter: SubjectAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout

    private lateinit var profilePanel: SlidingUpPanelLayout
    private lateinit var authorPanelImage: CircleImageView
    private lateinit var authorPanelUsername: TextView
    private lateinit var authorPanelEmail: TextView
    private lateinit var authorPanelEmailIcon: ImageView
    private lateinit var karmaCount: TextView

    private lateinit var personalProfilePicture: CircleImageView
    private lateinit var createPostEditText: TextInputEditText

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
        userTop1Name = view.findViewById(R.id.userTop1Name)
        userTop1Karma = view.findViewById(R.id.userTop1Karma)

        userTop2Picture = view.findViewById(R.id.userTop2Picture)
        userTop2Name = view.findViewById(R.id.userTop2Name)
        userTop2Karma = view.findViewById(R.id.userTop2Karma)

        userTop3Picture = view.findViewById(R.id.userTop3Picture)
        userTop3Name = view.findViewById(R.id.userTop3Name)
        userTop3Karma = view.findViewById(R.id.userTop3Karma)

        viewModel.topUsersPictures.observe(viewLifecycleOwner) {
            it?.let {
                userTop1Picture.setImageBitmap(it[0]?.data)
                userTop2Picture.setImageBitmap(it[1]?.data)
                userTop3Picture.setImageBitmap(it[2]?.data)
            }
        }

        viewModel.topUsers.observe(viewLifecycleOwner) {
            it?.let {
                userTop1Name.text = it[0].username
                userTop1Karma.text = it[0].karma.toString()
                userTop2Name.text = it[1].username
                userTop2Karma.text = it[1].karma.toString()
                userTop3Name.text = it[2].username
                userTop3Karma.text = it[2].karma.toString()
            }
        }
    }

    private fun initializePersonalTab(view: View) {
        createPostEditText = view.findViewById(R.id.createPostEditText)
        createPostEditText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.addSubjectFragment)
        }

        personalProfilePicture = view.findViewById(R.id.personalProfilePicture)
        userViewModel.picture.observe(viewLifecycleOwner) {
            it?.let { personalProfilePicture.setImageBitmap(it.data) }
        }
        personalProfilePicture.setOnClickListener {
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
                Log.d("ID", swa.post.getId())
                val bundle =
                    bundleOf(CommentListFragment.EXTRA_SUBJECT_COMMENTED_ID to swa.post.getId())
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