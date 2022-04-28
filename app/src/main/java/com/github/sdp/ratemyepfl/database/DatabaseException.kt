package com.github.sdp.ratemyepfl.database

data class DatabaseException(val error: String) : Exception(error)
