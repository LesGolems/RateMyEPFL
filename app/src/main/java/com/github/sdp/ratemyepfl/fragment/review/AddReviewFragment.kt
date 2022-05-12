package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.viewmodel.AddReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.AddReviewViewModel.Companion.NO_GRADE_MESSAGE
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
Fragment for the review creation, shared for every reviewable item
 */
@AndroidEntryPoint
class AddReviewFragment : Fragment(R.layout.fragment_add_review) {

    companion object {
        fun onTextChangedTextWatcher(consume: (CharSequence?, Int, Int, Int) -> Unit): TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    consume(p0, p1, p2, p3)
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
    }

    private lateinit var ratingBar: RatingBar
    private lateinit var comment: TextInputEditText
    private lateinit var title: TextInputEditText
    private lateinit var scoreTextView: TextView
    private lateinit var doneButton: Button
    private lateinit var anonymousSwitch: SwitchCompat

    private lateinit var reviewIndicationTitle: TextView

    @Inject
    lateinit var auth: ConnectedUser

    private val addReviewViewModel: AddReviewViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneButton = view.findViewById(R.id.doneButton)
        ratingBar = view.findViewById(R.id.reviewRatingBar)
        comment = view.findViewById(R.id.addReviewComment)
        title = view.findViewById(R.id.addReviewTitle)
        scoreTextView = view.findViewById(R.id.overallScoreTextView)
        anonymousSwitch = view.findViewById(R.id.anonymous_switch)

        reviewIndicationTitle = view.findViewById(R.id.reviewTitle)

        reviewIndicationTitle.text = getString(R.string.title_review, addReviewViewModel.id)

        addReviewViewModel.rating.observe(viewLifecycleOwner) { rating ->
            scoreTextView.text = getString(
                R.string.overall_score_review,
                rating?.toString() ?: NO_GRADE_MESSAGE
            )
        }

        setupListeners()
    }

    private fun setupListeners() {
        doneButton.setOnClickListener {
            addReview()
        }

        ratingBar.setOnRatingBarChangeListener { _, float, _ ->
            val rating = ReviewRating.fromValue(float)
            addReviewViewModel.setRating(rating)
        }

        comment.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            addReviewViewModel.setComment(text?.toString())
        })

        title.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            addReviewViewModel.setTitle(text?.toString())
        })

        anonymousSwitch.setOnCheckedChangeListener { _, b ->
            addReviewViewModel.setAnonymous(b)
        }
    }

    /**
     *  Adds the review to the database
     */
    private fun addReview() {
        try {
            addReviewViewModel.submitReview()
            reset()
            // Bar that will appear at the bottom of the screen
            displayOnSnackbar(getString(R.string.review_sent_text))
        } catch (due: DisconnectedUserException) {
            displayOnSnackbar(due.message)
        } catch (mie: MissingInputException) {
            if (title.text.isNullOrEmpty()) {
                title.error = mie.message
            } else if (comment.text.isNullOrEmpty()) {
                comment.error = mie.message
            } else {
                displayOnSnackbar(mie.message)
            }
        }
    }

    private fun displayOnSnackbar(message: String?) {
        if (message != null) {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.reviewBottomNavigationView)
                .show()
        }
    }

    /**
     * Once a review is submitted all the information are reset to default
     */
    private fun reset() {
        title.setText("")
        comment.setText("")
        ratingBar.rating = 0f
    }

    /**
     * Checks if current user is logged in, if not display a warning
     */
    private fun checkLogin(): Boolean {
        if (!auth.isLoggedIn()) {
            Snackbar.make(
                requireView(),
                "You need to login to be able to review",
                Snackbar.LENGTH_SHORT
            )
                .setAnchorView(R.id.reviewBottomNavigationView)
                .show()
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }
}