package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class Course constructor(
    val title: String,
    val section: String,
    val teacher: String,
    val credits: Int,
    val courseCode: String,
    override val grade: Double,
    override val numReviews: Int,
    val cycle: String? = null,
    val session: String? = null,
    val grading: String? = null,
    val language: String? = null,
) : Reviewable() {

    override fun toString(): String {
        return title
    }

    override fun getId(): String = courseCode

    /**
     * Creates an hash map of the Course
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            CourseRepositoryImpl.TITLE_FIELD_NAME to title,
            CourseRepositoryImpl.SECTION_FIELD_NAME to section,
            CourseRepositoryImpl.TEACHER_FIELD_NAME to teacher,
            CourseRepositoryImpl.CREDITS_FIELD_NAME to credits,
            CourseRepositoryImpl.COURSE_CODE_FIELD_NAME to courseCode,
            CourseRepositoryImpl.CYCLE_FIELD_NAME to cycle,
            CourseRepositoryImpl.SESSION_FIELD_NAME to session,
            CourseRepositoryImpl.GRADING_FIELD_NAME to grading,
            CourseRepositoryImpl.LANGUAGE_FIELD_NAME to language
        ).apply { this.putAll(super.toHashMap()) }
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
        private var grade: Double? = null,
        private var numReviews: Int? = null,
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

        fun setGrade(grade: Double?) = apply {
            this.grade = grade
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
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
            val grade = this asMandatory grade
            val numReviews = this asMandatory numReviews

            return Course(
                title,
                section,
                teacher,
                credits,
                courseCode,
                grade,
                numReviews,
                cycle,
                session,
                grading,
                language
            )
        }

    }


}