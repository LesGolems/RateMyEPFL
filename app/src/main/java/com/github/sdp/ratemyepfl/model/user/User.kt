package com.github.sdp.ratemyepfl.model.user
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid : String,
    val username : String?,
    val email : String?,
    val picture : String?
) {
    companion object {

        const val USERNAME_FIELD = "username"
        const val EMAIL_FIELD = "email"
        const val PICTURE_FIELD = "picture"

        fun DocumentSnapshot.toUser() : User {
            return User(
                uid = id,
                username = getString(USERNAME_FIELD)!!,
                email = getString(EMAIL_FIELD)!!,
                picture = getString(PICTURE_FIELD)!!
                )
            }
        }

    constructor(user : ConnectedUser) : this(
        uid = user.getUserId()!!,
        username = user.getUsername()!!,
        email = user.getEmail()!!,
        picture = user.getProfilePictureUrl()!!)

    fun toHashMap(): HashMap<String, String?> {
        return hashMapOf(
            USERNAME_FIELD to username,
            EMAIL_FIELD to email,
            PICTURE_FIELD to picture
        )
    }
}