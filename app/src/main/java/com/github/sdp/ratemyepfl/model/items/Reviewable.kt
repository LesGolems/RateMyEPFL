package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Serializable
abstract class Reviewable {
    abstract val id: String
    abstract val collectionPath: String
}