package com.github.sdp.ratemyepfl.exceptions

class DisconnectedUserException(override val message: String? = DEFAULT_ERROR_MSG) : Exception() {
    companion object {
        const val DEFAULT_ERROR_MSG = "You need to be connected to perform this action."
    }
}