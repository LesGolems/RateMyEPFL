package com.github.sdp.ratemyepfl.model.post

import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.serialization.Serializable

@Serializable
data class Comment constructor(
    override var postId: String = "",
    val subjectId: String = "",
    override val comment: String = "",
    override val date: DateTime = DateTime.DEFAULT_DATE_TIME,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf()
) : Post(postId, "", comment, date, uid, likers, dislikers) {

    override fun withId(id: String): Comment {
        return this.apply {
            this.postId = id
        }
    }

    data class Builder(
        private var subjectId: String? = null
    ) : Post.Builder<Comment>() {

        /**
         * Sets the id of the commented subject
         * @param id: commented subject id
         * @return this
         */
        fun setSubjectID(id: String?) = apply {
            this.subjectId = id
        }

        /**
         * Builds the corresponding [Comment]
         *
         * @throws IllegalStateException if one of the mandatory properties is null
         */
        override fun build(): Comment {
            val postId = this.postId ?: ""
            val subjectId = this asMandatory subjectId
            val comment = this asMandatory comment
            val date = this asMandatory date
            val uid = this.uid
            val likers = this asMandatory this.likers
            val dislikers = this asMandatory this.dislikers

            return Comment(
                postId,
                subjectId,
                comment,
                date,
                uid,
                likers,
                dislikers
            )
        }
    }

}