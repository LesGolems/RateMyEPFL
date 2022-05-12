package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.time.LocalDateTime

data class Event constructor(
    val name: String,
    val numParticipants: Int,
    val limitParticipants: Int,
    var participants: List<String> = listOf(),
    override val grade: Double,
    val lat: Double,
    val long: Double,
    val date: LocalDateTime
) : Reviewable(), Displayable {

    override fun toString(): String {
        return name
    }

    override fun toHashMap(): HashMap<String, Any?> = hashMapOf<String, Any?>(
        EventRepositoryImpl.NAME_FIELD_NAME to name,
        EventRepositoryImpl.NUMBER_PARTICIPANTS_FIELD_NAME to numParticipants,
        EventRepositoryImpl.LIMIT_PARTICIPANTS_FIELD_NAME to limitParticipants,
        EventRepositoryImpl.PARTICIPANTS_FIELD_NAME to participants,
        EventRepositoryImpl.LATITUDE_FIELD_NAME to lat,
        EventRepositoryImpl.LONGITUDE_FIELD_NAME to long,
        EventRepositoryImpl.DATE_FIELD_NAME to date.toString(),
    ).apply { this.putAll(super.toHashMap()) }

    fun showParticipation(): String {
        return "Participants: $numParticipants/$limitParticipants"
    }

    override fun getId(): String = name

    override fun toMapItem(): MapItem {
        return EventItem(
            this,
            MapActivityUtils.PHOTO_MAPPING.getOrDefault(
                name,
                R.raw.niki
            ), // Arbitrary default value
            BitmapDescriptorFactory.fromResource(R.raw.event_marker)
        )
    }

    /**
     * Builder to create an event step by step
     * Mandatory fields are:
     *  - [name]
     */
    class Builder(
        private var name: String? = null,
        private var numParticipants: Int? = 0,
        private var limitParticipants: Int? = null,
        private var participants: List<String>? = listOf(),
        private var grade: Double? = null,
        private var lat: Double? = null,
        private var long: Double? = null,
        private var date: LocalDateTime? = null,
    ) : ReviewableBuilder<Event> {


        fun name(name: String?) = apply {
            this.name = name
        }

        fun setNumParticipants(numParticipants: Int?) = apply {
            this.numParticipants = numParticipants
        }

        fun setLimitParticipants(limitParticipants: Int?) = apply {
            this.limitParticipants = limitParticipants
        }

        fun setParticipants(participants: List<String>?) = apply {
            this.participants = participants
        }

        fun setLat(lat: Double?) = apply {
            this.lat = lat
        }

        fun setLong(long: Double?) = apply {
            this.long = long
        }

        fun setDate(date: LocalDateTime?) = apply {
            this.date = date
        }

        fun setGrade(grade: Double?) = apply {
            this.grade = grade
        }

        override fun build(): Event {
            val name = this asMandatory name
            val numParticipants = this asMandatory numParticipants
            val limitParticipants = this asMandatory limitParticipants
            val participants = this asMandatory participants
            val lat = this asMandatory lat
            val long = this asMandatory long
            val date = this asMandatory date
            val grade = this asMandatory grade

            return Event(
                name,
                numParticipants,
                limitParticipants,
                participants,
                grade,
                lat,
                long,
                date
            )
        }
    }
}