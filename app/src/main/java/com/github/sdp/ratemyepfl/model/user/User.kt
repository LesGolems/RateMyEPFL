package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.UserRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Class
import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val username: String? = null,
    val email: String? = null,
    val karma: Int = 0,
    val isAdmin: Boolean = false,
    val timetable: ArrayList<Class> = DEFAULT_TIMETABLE
) : RepositoryItem {

    companion object {
        val DEFAULT_TIMETABLE = ArrayList<Class>()
    }

    constructor(user: ConnectedUser) : this(
        uid = user.getUserId()!!,
        username = user.getUsername()!!,
        email = user.getEmail()!!
    )

    override fun getId(): String = uid
}