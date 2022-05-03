package com.github.sdp.ratemyepfl.exceptions

data class DatabaseException(val error: String) : Exception(error)
