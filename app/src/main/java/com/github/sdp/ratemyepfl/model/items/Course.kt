package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val title: String,
    val section: String,
    val teacher: String,
    val credits: Int,
    val courseCode: String,
    override val numReviews: Int,
    override val averageGrade: Double,
    val cycle: String? = null,
    val session: String? = null,
    val grading: String? = null,
    val language: String? = null
) : Reviewable() {

    override fun toString(): String {
        return "$courseCode $title"
    }

    override fun getId(): String = courseCode

    /**
     * Creates an hash map of the Course
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            CourseRepositoryImpl.TITLE_FIELD_NAME to title,
            CourseRepositoryImpl.SECTION_FIELD_NAME to section,
            CourseRepositoryImpl.TEACHER_FIELD_NAME to teacher,
            CourseRepositoryImpl.CREDITS_FIELD_NAME to credits.toString(),
            CourseRepositoryImpl.COURSE_CODE_FIELD_NAME to courseCode,
            ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME to numReviews.toString(),
            ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME to averageGrade.toString(),
            CourseRepositoryImpl.CYCLE_FIELD_NAME to cycle,
            CourseRepositoryImpl.SESSION_FIELD_NAME to session,
            CourseRepositoryImpl.GRADING_FIELD_NAME to grading,
            CourseRepositoryImpl.LANGUAGE_FIELD_NAME to language
        )
    }

    /**
     * Builder to create a course step by step
     * Mandatory fields are:
     * - [courseCode]
     * - [title]
     * - [section]
     * - [teacher]
     * - [credits]
     */
    class Builder(
        private var title: String? = null,
        private var section: String? = null,
        private var teacher: String? = null,
        private var credits: Int? = null,
        private var courseCode: String? = null,
        private var numReviews: Int? = null,
        private var averageGrade: Double? = null,
        private var cycle: String? = null,
        private var session: String? = null,
        private var grading: String? = null,
        private var language: String? = null,
    ) : ReviewableBuilder<Course> {


        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setSection(section: String?) = apply {
            this.section = section
        }

        fun setTeacher(teacher: String?) = apply {
            this.teacher = teacher
        }

        fun setCredits(credits: Int?) = apply {
            this.credits = credits
        }

        fun setCourseCode(courseCode: String?) = apply {
            this.courseCode = courseCode
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
        }

        fun setAverageGrade(averageGrade: Double?) = apply {
            this.averageGrade = averageGrade
        }

        fun setCycle(cycle: String?) = apply {
            this.cycle = cycle
        }

        fun setSession(session: String?) = apply {
            this.session = session
        }

        fun setGrading(grading: String?) = apply {
            this.grading = grading
        }

        fun setLanguage(language: String?) = apply {
            this.language = language
        }

        override fun build(): Course {
            val title = this asMandatory title
            val section = this asMandatory section
            val teacher = this asMandatory teacher
            val credits = this asMandatory credits
            val courseCode = this asMandatory courseCode
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade

            return Course(
                title,
                section,
                teacher,
                credits,
                courseCode,
                numReviews,
                averageGrade,
                cycle,
                session,
                grading,
                language
            )
        }

    }


}