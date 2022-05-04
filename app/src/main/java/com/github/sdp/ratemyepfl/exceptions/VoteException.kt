package com.github.sdp.ratemyepfl.exceptions

data class VoteException(val error: String) : Exception(error)