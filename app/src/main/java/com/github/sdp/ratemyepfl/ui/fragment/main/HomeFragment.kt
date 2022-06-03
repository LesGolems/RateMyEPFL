package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.ObjectWithAuthor
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.ui.adapter.post.SubjectAdapter
import com.github.sdp.ratemyepfl.ui.fragment.PostListFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.getListener
import com.github.sdp.ratemyepfl.viewmodel.main.HomeViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : PostListFragment<Subject>(
    R.layout.fragment_home,
    R.layout.subject_item,
    R.id.subjectRecyclerView
) {

    private lateinit var personalProfilePicture: CircleImageView
    private lateinit var createPostEditText: TextInputEditText

    private lateinit var emptyListMessage: String

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePersonalTab(view)

        setHasOptionsMenu(true)

        // Displays the most recent posts first
        posts().observe(viewLifecycleOwner) {
            it?.let {
                postAdapter.submitList(it.sortedByDescending { pwa ->
                    pwa.obj.date.toString()
                })
            }
        }

        emptyListMessage = getString(R.string.empty_post_list_message, "subjects")
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
    }


    override fun setupAdapter(view: View): PostAdapter<Subject> =
        SubjectAdapter(
            viewLifecycleOwner, userViewModel,
            getListener({ subject, s -> viewModel.updateUpVotes(subject, s) }, view),
            getListener({ subject, s -> viewModel.updateDownVotes(subject, s) }, view),
            { swa -> viewModel.removeSubject(swa.obj.postId) },
            { swa ->
                val bundle =
                    bundleOf(CommentListFragment.EXTRA_SUBJECT_COMMENTED_ID to swa.obj.getId())
                Navigation.findNavController(view).navigate(R.id.commentListFragment, bundle)
            },
            { swa -> displayProfilePanel(swa.author, swa.image) })


    override fun posts(): MutableLiveData<List<ObjectWithAuthor<Subject>>> {
        return viewModel.subjects
    }

    override fun updatePostsList() {
        viewModel.viewModelScope
            .launch {
                displayPosts(viewModel.getSubjects(), emptyListMessage)
            }
    }

    override fun updateUpVotes(post: Subject, uid: String?) {
        viewModel.updateUpVotes(post, uid)
        updatePostsList()
    }

    override fun updateDownVotes(post: Subject, uid: String?) {
        viewModel.updateDownVotes(post, uid)
        updatePostsList()
    }

    override fun removePost(postId: String) {
        viewModel.removeSubject(postId)
        updatePostsList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.rankingButton) {
            Navigation.findNavController(requireView()).navigate(R.id.rankingFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}