package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.viewmodel.AddReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment for the review creation, shared for every reviewable item
 */
@AndroidEntryPoint
class AddReviewFragment : Fragment(R.layout.fragment_add_review) {

    companion object {
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
        const val NO_GRADE_MESSAGE: String = "You need to give a grade !"

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
    private lateinit var reviewIndicationTitle: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var doneButton: Button

    private val viewModel: AddReviewViewModel by viewModels()

    // Gets the shared view model
    private val activityViewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneButton = view.findViewById(R.id.doneButton)
        ratingBar = view.findViewById(R.id.reviewRatingBar)
        comment = view.findViewById(R.id.addReviewComment)
        title = view.findViewById(R.id.addReviewTitle)
        reviewIndicationTitle = view.findViewById(R.id.reviewTitle)
        scoreTextView = view.findViewById(R.id.overallScoreTextView)

        viewModel.rating.observe(viewLifecycleOwner) { rating ->
            scoreTextView.text =
                getString(
                    R.string.overall_score_review,
                    rating?.toString() ?: NO_GRADE_MESSAGE
                )
        }

        reviewIndicationTitle.text = getString(R.string.title_review, activityViewModel.id)
        setupListeners()
    }

    private fun setupListeners() {
        doneButton.setOnClickListener {
            addReview()
        }

        ratingBar.setOnRatingBarChangeListener { _, float, _ ->
            val rating = ReviewRating.fromValue(float)
            viewModel.setRating(rating)
        }

        comment.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setComment(text?.toString())
        })

        title.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setTitle(text?.toString())
        })
    }

    /**
     *  Adds the review to the database
     */
    private fun addReview() {
        if (viewModel.submitReview(activityViewModel.id)) {
            reset()
            // Bar that will appear at the bottom of the screen
            Snackbar.make(requireView(), R.string.review_sent_text, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.reviewNavigationView)
                .show()
        } else {
            setError(title, title.text.toString(), EMPTY_TITLE_MESSAGE)
            setError(comment, comment.text.toString(), EMPTY_COMMENT_MESSAGE)
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
     * Helper method to set the error message when an input is empty, i.e invalid
     */
    private fun setError(layout: TextInputEditText, actualValue: String?, errorMessage: String) {
        if (actualValue == null || actualValue == "") layout.error = errorMessage
    }

}