package com.github.sdp.ratemyepfl.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate


class AddReviewViewModelTest /*{
    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()
    val course = Course("SWENG", "CS", "Candea", 4, "CS-306")

    private val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
        set(AddReviewActivity.EXTRA_ITEM_REVIEWED, course))
    }
    private val repo = CoursesReviewsRepository()

    @Test
    fun correctlyInitializeTheCourseWithTheBundleState() {
        val course = Course("Sweng", "CS", "Candea", 4, "CS-306")
        val savedStateHandle = SavedStateHandle().apply {
            set(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER, Json.encodeToString(course))
        }
        val model = AddReviewViewModel(repo, savedStateHandle)
        Assert.assertEquals(course, model.course)
    }

    @Test
    fun throwsExceptionIfTheCourseIsNotValid() {
        val savedStateHandle = SavedStateHandle()
        val repository = FakeCoursesReviewsRepository()
        Assert.assertThrows(IllegalArgumentException::class.java) {
            AddReviewViewModel(
                repository,
                savedStateHandle
            )
        }
    }

    @Test
    fun setRatingCorrectly() {
        val rating = ReviewRating.AVERAGE
        val model = AddReviewViewModel(repo, savedStateHandle)
        model.setRating(rating)
        Assert.assertEquals(rating, model.getRating())
    }

    @Test
    fun setTitleCorrectly() {
        val title = "My title"
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        viewModel.title.postValue(title)

        Assert.assertEquals(title, viewModel.getTitle())
    }

    @Test
    fun setCommentCorrectly() {
        val comment = "My comment"
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        viewModel.setComment(comment)
        Assert.assertEquals(comment, viewModel.getComment())
    }

    @Test
    fun setDateCorrectly() {
        val date = LocalDate.of(2022, 3, 9)
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        viewModel.setDate(date)

        Assert.assertEquals(date, viewModel.getDate())
    }

    @Test
    fun correctlyReviewWhenUserEntersRatingTitleCommentAndDate() {
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        val rating = ReviewRating.AVERAGE
        val title = "My title"
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setRating(rating)
            this.setTitle(title)
            this.setComment(comment)
            this.setDate(date)
        }.review()

        Assert.assertEquals(rating, review?.rating)
        Assert.assertEquals(title, review?.title)
        Assert.assertEquals(comment, review?.comment)
        Assert.assertEquals(date, review?.date)
    }

    @Test
    fun returnNullWhenRatingIsMissing() {
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        val title = "My title"
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setTitle(title)
            this.setComment(comment)
            this.setDate(date)
        }.review()

        Assert.assertEquals(null, review)
    }

    @Test
    fun returnNullWhenTitleIsMissing() {
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        val rating = ReviewRating.AVERAGE
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setRating(rating)
            this.setComment(comment)
            this.setDate(date)
        }.review()

        Assert.assertEquals(null, review)
    }

    @Test
    fun returnNullWhenCommentIsMissing() {
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        val rating = ReviewRating.AVERAGE
        val title = "My title"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setTitle(title)
            this.setRating(rating)
            this.setDate(date)
        }.review()

        Assert.assertEquals(null, review)
    }

    @Test
    fun usesCurrentDateIfDateIsMissing() {
        val viewModel = AddReviewViewModel(repo, savedStateHandle)
        val rating = ReviewRating.AVERAGE
        val title = "My title"
        val comment = "My comment"
        lateinit var date: LocalDate
        val review: CourseReview? = viewModel.apply {
            this.setTitle(title)
            this.setRating(rating)
            this.setComment(comment)
        }.run {
            date = LocalDate.now()
            review()
        }

        Assert.assertEquals(date.year, review?.date?.year)
        Assert.assertEquals(date.month, review?.date?.month)
        Assert.assertEquals(date.dayOfMonth, review?.date?.dayOfMonth)

    }

}*/