package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.serialization.Serializable

@Serializable
data class Subject constructor(
    override var postId: String = "",
    override val title: String = "",
    override val comment: String = "",
    override val date: DateTime = DateTime.DEFAULT_DATE_TIME,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf(),
    val comments: List<String> = listOf(),
    val kind: SubjectKind = SubjectKind.OTHER
) : Post(postId, title, comment, date, uid, likers, dislikers) {

    override fun withId(id: String): Subject {
        return this.apply {
            this.postId = id
        }
    }

    /**
     * Allows to create a [Subject] incrementally
     */
    data class Builder(
        private var comments: List<String>? = listOf(),
        private var kind: SubjectKind? = null
    ) : Post.Builder<Subject>() {

        /**
         * Sets the comments of the subject
         * @param comments: comments of the subject
         * @return this
         */
        fun setComments(comments: List<String>?) = apply {
            this.comments = comments
        }

        /**
         * Sets the kind of the subject
         * @param kind: kind of the subject
         * @return this
         */
        fun setKind(kind: SubjectKind?) = apply {
            this.kind = kind
        }

        /**
         * Builds the corresponding [Subject]
         *
         * @throws IllegalStateException if one of the properties is null
         */
        override fun build(): Subject {
            val postId = this.postId ?: ""
            val title = this asMandatory title
            val comment = this.comment ?: ""
            val date = this asMandatory date
            val uid = this.uid
            val likers = this asMandatory this.likers
            val dislikers = this asMandatory this.dislikers
            val comments = this asMandatory comments
            val kind = this asMandatory kind

            return Subject(postId,
                title, comment, date, uid, likers, dislikers, comments, kind
            )
        }


    }
}