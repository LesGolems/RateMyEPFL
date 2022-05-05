package com.github.sdp.ratemyepfl.exceptions

import java.lang.Exception

class MissingInputException(override val message: String? = DEFAULT_ERROR_MSG) : Exception() {
    companion object {
        const val DEFAULT_ERROR_MSG = "You must fill in all the fields to perform this action."
    }
}