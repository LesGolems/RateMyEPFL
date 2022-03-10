package com.github.sdp.ratemyepfl.viewmodel

import com.github.sdp.ratemyepfl.items.Course
import com.github.sdp.ratemyepfl.review.CourseReview
import com.github.sdp.ratemyepfl.review.ReviewRating
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class CourseReviewViewModelTest {
    private val defaultCourse = Course("Sweng", "CS", "Candea", 4, "CS-306")

    @Test
    fun factoryCreatesAViewModelWithTheCorrectCourse() {
        val course = Course("Sweng", "CS", "Candea", 4, "CS-306")
        val model = CourseReviewViewModel(course)
        assertEquals(course, model.course)
    }

    @Test
    fun setRatingCorrectly() {
        val rating = ReviewRating.AVERAGE
        val model = CourseReviewViewModel(defaultCourse)
        model.setRating(rating)
        Thread.sleep(DEFAULT_POSTING_TIME)
        assertEquals(rating, model.getRating())
    }

    @Test
    fun setTitleCorrectly() {
        val title = "My title"
        val viewModel = CourseReviewViewModel(defaultCourse)
        viewModel.title.postValue(title)
        Thread.sleep(DEFAULT_POSTING_TIME)

        assertEquals(title, viewModel.getTitle())
    }

    @Test
    fun setCommentCorrectly() {
        val comment = "My comment"
        val viewModel = CourseReviewViewModel(defaultCourse)
        viewModel.setTitle(comment)
        Thread.sleep(DEFAULT_POSTING_TIME)

        assertEquals(comment, viewModel.getComment())
    }

    @Test
    fun setDateCorrectly() {
        val date = LocalDate.of(2022, 3, 9)
        val viewModel = CourseReviewViewModel(defaultCourse)
        viewModel.setDate(date)
        Thread.sleep(DEFAULT_POSTING_TIME)

        assertEquals(date, viewModel.getDate())
    }

    @Test
    fun correctlyReviewWhenUserEntersRatingTitleCommentAndDate() {
        val viewModel = CourseReviewViewModel(defaultCourse)
        val rating = ReviewRating.AVERAGE
        val title = "My title"
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setRating(rating)
            this.setTitle(title)
            this.setComment(comment)
            this.setDate(date)
            Thread.sleep(DEFAULT_POSTING_TIME)
        }.review()

        assertEquals(rating, review?.rating)
        assertEquals(title, review?.title)
        assertEquals(comment, review?.comment)
        assertEquals(date, review?.date)
    }

    @Test
    fun returnNullWhenRatingIsMissing() {
        val viewModel = CourseReviewViewModel(defaultCourse)
        val title = "My title"
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setTitle(title)
            this.setComment(comment)
            this.setDate(date)
            Thread.sleep(DEFAULT_POSTING_TIME)
        }.review()

        assertEquals(null, review)
    }

    @Test
    fun returnNullWhenTitleIsMissing() {
        val viewModel = CourseReviewViewModel(defaultCourse)
        val rating = ReviewRating.AVERAGE
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setRating(rating)
            this.setComment(comment)
            this.setDate(date)
            Thread.sleep(DEFAULT_POSTING_TIME)
        }.review()

        assertEquals(null, review)
    }

    @Test
    fun returnNullWhenCommentIsMissing() {
        val viewModel = CourseReviewViewModel(defaultCourse)
        val rating = ReviewRating.AVERAGE
        val title = "My title"
        val date = LocalDate.of(2022, 3, 9)

        val review = viewModel.apply {
            this.setTitle(title)
            this.setRating(rating)
            this.setDate(date)
            Thread.sleep(DEFAULT_POSTING_TIME)
        }.review()

        assertEquals(null, review)
    }

    @Test
    fun usesCurrentDateIfDateIsMissing() {
        val viewModel = CourseReviewViewModel(defaultCourse)
        val rating = ReviewRating.AVERAGE
        val title = "My title"
        val comment = "My comment"
        lateinit var date: LocalDate
        val review: CourseReview? = viewModel.apply {
            this.setTitle(title)
            this.setRating(rating)
            this.setComment(comment)
            Thread.sleep(DEFAULT_POSTING_TIME)
        }.run {
            date = LocalDate.now()
            review()
        }

        assertEquals(date.year, review?.date?.year)
        assertEquals(date.month, review?.date?.month)
        assertEquals(date.dayOfMonth, review?.date?.dayOfMonth)

    }

    companion object {
        const val DEFAULT_POSTING_TIME: Long = 1000
    }
}