package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.CourseRepository
import com.github.sdp.ratemyepfl.database.Repository
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val title: String,
    val section: String,
    val teacher: String,
    val credits: Int,
    override val id: String,
    override val numReviews: Int,
    override val averageGrade: Double,
    val cycle: String? = null,
    val session: String? = null,
    val grading: String? = null,
    val language: String? = null
) : Reviewable() {

    override fun toString(): String {
        return "$id $title"
    }

    /**
     * Creates an hash map of the Course
     */
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            CourseRepository.TITLE_FIELD_NAME to title,
            CourseRepository.SECTION_FIELD_NAME to section,
            CourseRepository.TEACHER_FIELD_NAME to teacher,
            CourseRepository.CREDITS_FIELD_NAME to credits.toString(),
            Repository.NUM_REVIEWS_FIELD_NAME to numReviews.toString(),
            Repository.AVERAGE_GRADE_FIELD_NAME to averageGrade.toString(),
            CourseRepository.CYCLE_FIELD_NAME to cycle,
            CourseRepository.SESSION_FIELD_NAME to session,
            CourseRepository.GRADING_FIELD_NAME to grading,
            CourseRepository.LANGUAGE_FIELD_NAME to language
        )
    }

    /**
     * Builder to create a course step by step
     * Mandatory fields are:
     * - [id]
     * - [title]
     * - [section]
     * - [teacher]
     * - [credits]
     */
    class Builder : ReviewableBuilder<Course> {
        private var title: String? = null
        private var section: String? = null
        private var teacher: String? = null
        private var credits: Int? = null
        private var id: String? = null
        private var numReviews: Int? = null
        private var averageGrade: Double? = null
        private var cycle: String? = null
        private var session: String? = null
        private var grading: String? = null
        private var language: String? = null

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

        fun setId(id: String?) = apply {
            this.id = id
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
            val id = this asMandatory id
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade

            return Course(title, section, teacher, credits, id, numReviews, averageGrade, cycle, session, grading, language)
        }

    }


}