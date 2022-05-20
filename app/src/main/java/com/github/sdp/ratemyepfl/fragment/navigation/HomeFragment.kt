package com.github.sdp.ratemyepfl.fragment.navigation

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
import com.github.sdp.ratemyepfl.adapter.OnClickListener
import com.github.sdp.ratemyepfl.adapter.SubjectAdapter
import com.github.sdp.ratemyepfl.auth.ConnectedUser
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

    private lateinit var subjectAdapter: SubjectAdapter
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

    private val viewModel by activityViewModels<HomeViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresher = view.findViewById(R.id.subjectSwipeRefresh)
        recyclerView = view.findViewById(R.id.subjectRecyclerView)

        subjectAdapter = SubjectAdapter(viewLifecycleOwner, userViewModel,
            getListener { r, s -> viewModel.updateUpVotes(r, s) },
            getListener { r, s -> viewModel.updateDownVotes(r, s) },
            { swa ->
                lifecycleScope.launch {
                    viewModel.removeSubject(swa.post.getId())
                }
            }
        ) { swa -> displayProfilePanel(swa.author, swa.image) }

        recyclerView.adapter = subjectAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher.setOnRefreshListener {
            viewModel.updateSubjectsList()
            swipeRefresher.isRefreshing = false
        }

        viewModel.subjects.observe(viewLifecycleOwner) {
            it?.let { subjectAdapter.submitList(it) }
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
    }
}