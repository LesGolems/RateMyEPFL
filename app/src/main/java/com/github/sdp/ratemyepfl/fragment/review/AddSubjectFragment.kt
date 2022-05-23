package com.github.sdp.ratemyepfl.fragment.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.SubjectKind
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnToast
import com.github.sdp.ratemyepfl.viewmodel.AddSubjectViewModel
import com.github.sdp.ratemyepfl.viewmodel.AddSubjectViewModel.Companion.ONLY_ONE_KIND_MESSAGE
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddSubjectFragment : Fragment(R.layout.fragment_add_subject) {

    private lateinit var title: TextInputEditText
    private lateinit var comment: TextInputEditText
    private lateinit var doneButton: Button
    private lateinit var anonymousSwitch: SwitchCompat
    private lateinit var kinds: ChipGroup

    @Inject
    lateinit var auth: ConnectedUser

    private val addSubjectViewModel: AddSubjectViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneButton = view.findViewById(R.id.doneButton)
        comment = view.findViewById(R.id.addReviewComment)
        title = view.findViewById(R.id.addReviewTitle)
        anonymousSwitch = view.findViewById(R.id.anonymous_switch)
        kinds = view.findViewById(R.id.subjectKinds)

        setupKinds()
        setupListeners()
    }

    @SuppressLint("ResourceType")
    private fun setupKinds() {
        for (kind in SubjectKind.values()) {
            val context = requireContext()
            val chip = Chip(context).apply {
                text = kind.id
                chipIcon = ContextCompat.getDrawable(context, kind.icon)
                isCheckable = true
                chipEndPadding = 6F
            }
            kinds.addView(chip)
        }
    }

    private fun setupListeners() {
        doneButton.setOnClickListener {
            addSubject()
        }

        comment.addTextChangedListener(AddReviewFragment.onTextChangedTextWatcher { text, _, _, _ ->
            addSubjectViewModel.setComment(text?.toString())
        })

        title.addTextChangedListener(AddReviewFragment.onTextChangedTextWatcher { text, _, _, _ ->
            addSubjectViewModel.setTitle(text?.toString())
        })

        anonymousSwitch.setOnCheckedChangeListener { _, b ->
            addSubjectViewModel.setAnonymous(b)
        }

        kinds.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 1) {
                displayOnToast(requireContext(), ONLY_ONE_KIND_MESSAGE)
                addSubjectViewModel.setKind(null)
            } else if (checkedIds.size == 1) {
                val ordinal = checkedIds[0] - 1 // The group is counted as the first child
                addSubjectViewModel.setKind(SubjectKind.values()[ordinal])
            }
        }
    }

    /**
     *  Adds the review to the database
     */
    private fun addSubject() {
        val context = requireContext()
        try {
            addSubjectViewModel.submitSubject()
            reset()
            // Bar that will appear at the bottom of the screen
            displayOnToast(context, "Your post was submitted!")
            Navigation.findNavController(requireView()).popBackStack()
        } catch (due: DisconnectedUserException) {
            displayOnToast(context, due.message)
        } catch (mie: MissingInputException) {
            if (title.text.isNullOrEmpty()) {
                title.error = mie.message
            } else if (comment.text.isNullOrEmpty()) {
                comment.error = mie.message
            } else {
                displayOnToast(context, mie.message)
            }
        }
    }

    /**
     * Once a review is submitted all the information are reset to default
     */
    private fun reset() {
        title.setText("")
        comment.setText("")
    }


}