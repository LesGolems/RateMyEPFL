package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.ui.adapter.post.SubjectAdapter
import com.github.sdp.ratemyepfl.ui.fragment.PostListFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.main.HomeViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class HomeFragment : PostListFragment<Subject>(
    R.layout.fragment_home,
    R.id.subjectRecyclerView,
    R.id.subjectSwipeRefresh,
    R.layout.subject_item
) {

    private lateinit var userTop1Picture: CircleImageView
    private lateinit var userTop1Name: TextView
    private lateinit var userTop1Karma: TextView

    private lateinit var userTop2Picture: CircleImageView
    private lateinit var userTop2Name: TextView
    private lateinit var userTop2Karma: TextView

    private lateinit var userTop3Picture: CircleImageView
    private lateinit var userTop3Name: TextView
    private lateinit var userTop3Karma: TextView

    private lateinit var personalProfilePicture: CircleImageView
    private lateinit var createPostEditText: TextInputEditText

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePodium(view)
        initializePersonalTab(view)

        swipeRefresher.setOnRefreshListener {
            updatePostsList()
            viewModel.updatePodium()
            swipeRefresher.isRefreshing = false
        }
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


    override fun setupAdapter(view: View): PostAdapter<Subject> =
        SubjectAdapter(
            viewLifecycleOwner, userViewModel,
            getListener({ subject, s -> viewModel.updateUpVotes(subject, s) }, view),
            getListener({ subject, s -> viewModel.updateDownVotes(subject, s) }, view),
            { swa -> viewModel.removeSubject(swa.post.postId) },
            { swa ->
                val bundle =
                    bundleOf(CommentListFragment.EXTRA_SUBJECT_COMMENTED_ID to swa.post.getId())
                Navigation.findNavController(view).navigate(R.id.commentListFragment, bundle)
            },
            { swa -> displayProfilePanel(swa.author, swa.image) })


    override fun posts(): MutableLiveData<List<PostWithAuthor<Subject>>> {
        return viewModel.subjects
    }

    override fun updatePostsList() {
        viewModel.updateSubjectsList()
    }

    override fun updateUpVotes(post: Subject, uid: String?) {
        viewModel.updateUpVotes(post, uid)
    }

    override fun updateDownVotes(post: Subject, uid: String?) {
        viewModel.updateDownVotes(post, uid)
    }

    override fun removePost(postId: String) {
        viewModel.removeSubject(postId)
    }
}