package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
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

    private lateinit var personalProfilePicture: CircleImageView
    private lateinit var createPostEditText: TextInputEditText

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePersonalTab(view)
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