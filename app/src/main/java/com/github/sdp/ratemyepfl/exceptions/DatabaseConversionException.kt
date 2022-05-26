package com.github.sdp.ratemyepfl.exceptions

class DatabaseConversionException(val to: Class<out Any>, val errorMessage: String) :
    Exception(errorMessage) {
    override val message: String
        get() = "Failed the conversion from DocumentSnapshot to $to ( $errorMessage )"
}