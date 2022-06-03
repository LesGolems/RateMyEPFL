package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.post.Review
import com.github.sdp.ratemyepfl.model.post.ReviewRating
import com.github.sdp.ratemyepfl.ui.fragment.AddPostFragment
import com.github.sdp.ratemyepfl.utils.FragmentUtils.displayOnSnackbar
import com.github.sdp.ratemyepfl.viewmodel.review.AddReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.review.AddReviewViewModel.Companion.NO_GRADE_TEXT
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for the review creation, shared for every reviewable item
 */
@AndroidEntryPoint
class AddReviewFragment : AddPostFragment<Review>(R.layout.fragment_add_review) {

    private lateinit var ratingBar: RatingBar
    private lateinit var scoreTextView: TextView

    private val addReviewViewModel: AddReviewViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        indicationTitle.text = addReviewViewModel.item.toStringAddReview()

        ratingBar = view.findViewById(R.id.reviewRatingBar)
        ratingBar.setOnRatingBarChangeListener { _, float, _ ->
            val rating = ReviewRating.fromValue(float)
            addReviewViewModel.setRating(rating)
        }

        scoreTextView = view.findViewById(R.id.overallScoreTextView)
        addReviewViewModel.rating.observe(viewLifecycleOwner) { rating ->
            scoreTextView.text = rating?.toString() ?: NO_GRADE_TEXT
        }
    }

    override fun setTitle(title: String?) {
        addReviewViewModel.setTitle(title)
    }

    override fun setComment(comment: String?) {
        addReviewViewModel.setComment(comment)
    }

    override fun setAnonymous(anonymous: Boolean) {
        addReviewViewModel.setAnonymous(anonymous)
    }

    override fun addPost() {
        val view = requireView()
        try {
            addReviewViewModel.submitReview()
            reset()
            displayOnSnackbar(view, getString(R.string.review_sent_text))
            Navigation.findNavController(requireView()).popBackStack()
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
        }
    }

    override fun reset() {
        super.reset()
        ratingBar.rating = 0f
    }
}