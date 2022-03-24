package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser

/**
 * Decorator class for ConnectedUser, otherwise I can't upgrade ConnectedUser
 * without modifying a bunch of files.
 */

interface CurrentUser : ConnectedUser {

    fun getUsername(): String?

    fun getProfilePictureUrl(): String?

}