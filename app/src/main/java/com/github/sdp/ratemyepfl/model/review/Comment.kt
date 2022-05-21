package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.database.post.CommentRepositoryImpl
import java.time.LocalDate

data class Comment constructor(
    val subjectId: String,
    override val comment: String,
    override val date: LocalDate,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf()
) : Post("", comment, date, uid, likers, dislikers) {

    override var postId: String = this.hashCode().toString()

    override fun withId(id: String): Comment {
        return this.apply {
            this.postId = id
        }
    }

    /**
     * Creates a hash map of the comment
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            CommentRepositoryImpl.SUBJECT_ID_FIELD_NAME to subjectId
        ).apply { this.putAll(super.toHashMap()) }
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
         * Builds the corresponding Comment
         *
         * @throws IllegalStateException if one of the properties is null
         */
        override fun build(): Comment {
            val subjectId = this asMandatory subjectId
            val comment = this asMandatory comment
            val date = this asMandatory date
            val uid = this.uid
            val likers = this asMandatory this.likers
            val dislikers = this asMandatory this.dislikers

            return Comment(
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