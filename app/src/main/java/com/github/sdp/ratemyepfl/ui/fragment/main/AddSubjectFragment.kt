package com.github.sdp.ratemyepfl.ui.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.review.SubjectKind
import com.github.sdp.ratemyepfl.ui.fragment.AddPostFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnSnackbar
import com.github.sdp.ratemyepfl.viewmodel.main.AddSubjectViewModel
import com.github.sdp.ratemyepfl.viewmodel.main.AddSubjectViewModel.Companion.ONLY_ONE_KIND_MESSAGE
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSubjectFragment : AddPostFragment<Subject>(R.layout.fragment_add_subject) {

    private lateinit var subjectKinds: ChipGroup

    private val addSubjectViewModel: AddSubjectViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subjectKinds = view.findViewById(R.id.subjectKinds)
        setupKinds()
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("ResourceType")
    private fun setupKinds() {
        subjectKinds.removeAllViews()
        for (kind in SubjectKind.values()) {
            val context = requireContext()
            val chip = Chip(context).apply {
                text = kind.id
                chipIcon = ContextCompat.getDrawable(context, kind.icon)
                isCheckable = true
            }
            subjectKinds.addView(chip)
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        subjectKinds.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.size > 1)
                displayOnSnackbar(requireView(), ONLY_ONE_KIND_MESSAGE)
            val kinds = checkedIds.map {
                val chip: Chip = group.findViewById(it)
                SubjectKind.fromId(chip.text.toString())!!
            }
            addSubjectViewModel.setKinds(kinds)
        }
    }

    override fun setComment(comment: String?) {
        addSubjectViewModel.setComment(comment)
    }

    override fun setTitle(title: String?) {
        addSubjectViewModel.setTitle(title)
    }

    override fun setAnonymous(anonymous: Boolean) {
        addSubjectViewModel.setAnonymous(anonymous)
    }

    override fun addPost() {
        val view = requireView()
        try {
            addSubjectViewModel.submitSubject()
            reset()
            displayOnSnackbar(view, getString(R.string.subject_sent_text))
            Navigation.findNavController(view).popBackStack()
        } catch (due: DisconnectedUserException) {
            displayOnSnackbar(view, due.message)
        } catch (mie: MissingInputException) {
            if (title.text.isNullOrEmpty()) {
                title.error = mie.message
            } else {
                displayOnSnackbar(view, mie.message)
            }
        } catch (ise: IllegalStateException) {
            displayOnSnackbar(view, ise.message)
        }
    }

    override fun reset() {
        super.reset()
        subjectKinds.clearCheck()
    }

}