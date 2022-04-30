package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.time.LocalDateTime

data class Event(
    val name: String,
    override val numReviews: Int,
    override val averageGrade: Double,
    val numParticipants: Int,
    val limitParticipants: Int,
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
        EventRepositoryImpl.LATITUDE_FIELD_NAME to lat,
        EventRepositoryImpl.LONGITUDE_FIELD_NAME to long,
        EventRepositoryImpl.DATE_FIELD_NAME to date.toString(),
    ).apply { putAll(super.toHashMap()) }

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
        private var numReviews: Int? = null,
        private var averageGrade: Double? = null,
        private var numParticipants: Int? = 0,
        private var limitParticipants: Int? = null,
        private var lat: Double? = null,
        private var long: Double? = null,
        private var date: LocalDateTime? = null,
    ) : ReviewableBuilder<Event> {


        fun name(name: String?) = apply {
            this.name = name
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
        }

        fun setAverageGrade(averageGrade: Double?) = apply {
            this.averageGrade = averageGrade
        }

        fun setNumParticipants(numParticipants: Int?) = apply {
            this.numParticipants = numParticipants
        }

        fun setLimitParticipants(limitParticipants: Int?) = apply {
            this.limitParticipants = limitParticipants
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

        override fun build(): Event {
            val name = this asMandatory name
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade
            val numParticipants = this asMandatory numParticipants
            val limitParticipants = this asMandatory limitParticipants
            val lat = this asMandatory lat
            val long = this asMandatory long
            val date = this asMandatory date

            return Event(
                name,
                numReviews,
                averageGrade,
                numParticipants,
                limitParticipants,
                lat,
                long,
                date
            )
        }
    }
}