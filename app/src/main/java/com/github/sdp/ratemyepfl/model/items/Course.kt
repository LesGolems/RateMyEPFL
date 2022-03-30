package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val title: String,
    val section: String,
    val teacher: String,
    val credits: Int,
    override val id: String,
    val cycle: String? = null,
    val session: String? = null,
    val grading: String? = null,
    val language: String? = null
) : Reviewable() {

    override fun toString(): String {
        return "$id $title"
    }

    /**
     * Builder to create a course step by step
     */
    class Builder : ReviewableBuilder<Course> {
        var title: String? = null
        var section: String? = null
        var teacher: String? = null
        var credits: Int? = null
        var id: String? = null
        var cycle: String? = null
        var session: String? = null
        var grading: String? = null
        var language: String? = null

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
            val title = this.title
            val section = this.section
            val teacher = this.teacher
            val credits = this.credits
            val id = this.id
            val cycle = this.cycle
            val session = this.session
            val grading = this.grading
            val language = this.language

            // Check for mandatory properties (i.e., non nullable)
            return if (title != null &&
                section != null &&
                teacher != null &&
                credits != null &&
                id != null
            ) {
                Course(title, section, teacher, credits, id, cycle, session, grading, language)
            } else throw IllegalStateException("Missing mandatory elements for a Course")
        }

    }


}