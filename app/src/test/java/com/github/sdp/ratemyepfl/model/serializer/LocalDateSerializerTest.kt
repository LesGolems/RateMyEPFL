package com.github.sdp.ratemyepfl.model.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class LocalDateSerializerTest {

    companion object {
        private val EXPECTED_DATE = LocalDate.of(2022, 12, 4)
        private const val EXPECTED_JSON = "\"2022-12-04\""
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun serializationWorks() {
        val json = LocalDateSerializer.serialize(EXPECTED_DATE)
        Assert.assertEquals(EXPECTED_JSON, json)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun deserializationWorks() {
        val date = LocalDateSerializer.deserialize(EXPECTED_JSON)
        Assert.assertEquals(EXPECTED_DATE, date)
    }
}