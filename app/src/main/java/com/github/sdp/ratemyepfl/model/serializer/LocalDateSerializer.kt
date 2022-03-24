package com.github.sdp.ratemyepfl.model.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.LocalDate

/**
 * A personalized serializer for a LocalDate
 */
@ExperimentalSerializationApi
@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    /**
     * Syntactic-sugar for serialization
     */
    fun serialize(date: LocalDate): String = Json.encodeToString(this, date)

    /**
     * Syntactic sugar for deserialization
     */
    fun deserialize(date: String): LocalDate = Json.decodeFromString(this, date)
}