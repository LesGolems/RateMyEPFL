package com.github.sdp.ratemyepfl.ui.fragment.main

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
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.SubjectKind
import com.github.sdp.ratemyepfl.ui.fragment.review.AddReviewFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnSnackbar
import com.github.sdp.ratemyepfl.viewmodel.main.AddSubjectViewModel
import com.github.sdp.ratemyepfl.viewmodel.main.AddSubjectViewModel.Companion.ONLY_ONE_KIND_MESSAGE
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
        kinds.removeAllViews()
        for (kind in SubjectKind.values()) {
            val context = requireContext()
            val chip = Chip(context).apply {
                text = kind.id
                tag = "chip_${kind.id}"
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

        kinds.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            if (checkedIds.size > 1) {
                displayOnSnackbar(requireView(), ONLY_ONE_KIND_MESSAGE)
            }
            val kinds = checkedIds.map {
                val chip = chipGroup.findViewById<Chip>(it)
                SubjectKind.fromId(chip.text.toString())!!
            }
            addSubjectViewModel.setKinds(kinds)
        }
    }

    /**
     *  Adds the review to the database
     */
    private fun addSubject() {
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
            } else if (comment.text.isNullOrEmpty()) {
                comment.error = mie.message
            } else {
                displayOnSnackbar(view, mie.message)
            }
        } catch (ise: IllegalStateException) {
            displayOnSnackbar(view, ise.message)
        }
    }

    /**
     * Once a review is submitted all the information are reset to default
     */
    private fun reset() {
        title.setText("")
        comment.setText("")
        kinds.clearCheck()
    }


}