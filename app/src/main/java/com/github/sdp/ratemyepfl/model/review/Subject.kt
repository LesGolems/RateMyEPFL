package com.github.sdp.ratemyepfl.model.review

import java.time.LocalDate

data class Subject constructor(
    override val title: String,
    override val comment: String,
    override val date: LocalDate,
    override val uid: String? = null,
    override var likers: List<String> = listOf(),
    override var dislikers: List<String> = listOf()
) : Post(title, comment, date, uid, likers, dislikers) {

    override var postId: String = this.hashCode().toString()

    //override fun serialize(): String = Companion.serialize(this)

    override fun withId(id: String): Subject {
        return this.apply {
            this.postId = id
        }
    }
}