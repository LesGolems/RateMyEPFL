package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
abstract class Reviewable {
    abstract val id: String
    abstract val collectionPath: String
}