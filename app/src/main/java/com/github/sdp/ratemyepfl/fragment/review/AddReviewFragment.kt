package com.github.sdp.ratemyepfl.fragment.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.viewmodel.AddReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

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
    private val activityViewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneButton = view.findViewById(R.id.doneButton)
        ratingBar = view.findViewById(R.id.reviewRatingBar)
        comment = view.findViewById(R.id.addReviewComment)
        title = view.findViewById(R.id.addReviewTitle)
        reviewIndicationTitle = view.findViewById(R.id.reviewTitle)
        scoreTextView = view.findViewById(R.id.overallScoreTextView)
        title.error = "Please enter a title"

        viewModel.rating.observe(viewLifecycleOwner) { rating ->
            scoreTextView.text =
                getString(
                    R.string.overall_score_review,
                    rating?.toString() ?: NO_GRADE_MESSAGE
                )
        }

        viewModel.comment.observe(viewLifecycleOwner){ text ->
            setError(comment, text, EMPTY_COMMENT_MESSAGE)
        }

        viewModel.title.observe(viewLifecycleOwner){ text ->
            setError(title, text, EMPTY_TITLE_MESSAGE)
        }

        activityViewModel.getReviewable().observe(viewLifecycleOwner){
            reviewIndicationTitle.text = getString(R.string.title_review, it!!.id)
        }

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

    /* Adds the review to the database */
    private fun addReview() {
        if(viewModel.submitReview(activityViewModel.getReviewable().value)) {
            reset()
        }
    }

    private fun reset(){
        title.text = null
        comment.text = null
        ratingBar.rating = 0f
    }

    private fun setError(layout : TextInputEditText, actualValue : String?, errorMessage : String){
        if(actualValue == null || actualValue == "") layout.error = errorMessage
    }

}