package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.database.post.SubjectRepositoryImpl
import java.time.LocalDate

data class Subject constructor(
    override val title: String,
    override val comment: String,
    override val date: LocalDate,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf(),
    val commentators: List<String> = listOf()
) : Post(title, comment, date, uid, likers, dislikers) {

    override var postId: String = this.hashCode().toString()

    override fun withId(id: String): Subject {
        return this.apply {
            this.postId = id
        }
    }

    /**
     * Creates a hash map of the subject
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            SubjectRepositoryImpl.COMMENTATORS_FIELD_NAME to commentators,
        ).apply { this.putAll(super.toHashMap()) }
    }

    /**
     * Allows to create a [Subject] incrementally
     */
    data class Builder(
        private var commentators: List<String>? = listOf()
    ) : Post.Builder<Subject>() {

        fun setCommentators(commentators: List<String>?) = apply {
            this.commentators = commentators
        }

        override fun build(): Subject {
            val title = this asMandatory title
            val comment = this asMandatory comment
            val date = this asMandatory date
            val uid = this.uid
            val likers = this asMandatory this.likers
            val dislikers = this asMandatory this.dislikers
            val commentators = this asMandatory commentators

            return Subject(
                title, comment, date, uid, likers, dislikers, commentators
            )
        }


    }
}