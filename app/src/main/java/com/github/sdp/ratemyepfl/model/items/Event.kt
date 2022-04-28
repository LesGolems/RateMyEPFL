package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.EventRepository
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.time.LocalDateTime

class Event(
    override val id: String,
    override val numReviews: Int,
    override val averageGrade: Double,
    val numParticipants: Int,
    val limitParticipants: Int,
    val lat: Double,
    val long: Double,
    val date: LocalDateTime
) : Reviewable(), Displayable {

    override fun toString(): String {
        return id
    }

    fun showParticipation(): String {
        return "Participants: $numParticipants/$limitParticipants"
    }

    /**
     * Creates an hash map of the Event, to add it to the DB
     */
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            EventRepository.NUMBER_PARTICIPANTS_FIELD_NAME to numParticipants.toString(),
            EventRepository.LIMIT_PARTICIPANTS_FIELD_NAME to limitParticipants.toString(),
            EventRepository.LATITUDE_FIELD_NAME to lat.toString(),
            EventRepository.LONGITUDE_FIELD_NAME to long.toString(),
            EventRepository.DATE_FIELD_NAME to date.toString(),
            Repository.NUM_REVIEWS_FIELD_NAME to numReviews.toString(),
            Repository.AVERAGE_GRADE_FIELD_NAME to averageGrade.toString()
        )
    }

    override fun toMapItem(): MapItem {
        return EventItem(
            this,
            MapActivityUtils.PHOTO_MAPPING.getOrDefault(id, R.raw.niki), // Arbitrary default value
            BitmapDescriptorFactory.fromResource(R.raw.event_marker)
        )
    }

    /**
     * Builder to create an event step by step
     * Mandatory fields are:
     *  - [id]
     */
    class Builder : ReviewableBuilder<Event> {
        private var id: String? = null
        private var numReviews: Int? = null
        private var averageGrade: Double? = null
        private var numParticipants: Int? = null
        private var limitParticipants: Int? = null
        private var lat: Double? = null
        private var long: Double? = null
        private var date: LocalDateTime? = null

        fun setId(id: String?) = apply {
            this.id = id
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
            val id = this asMandatory id
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade
            val numParticipants = numParticipants ?: 0
            val limitParticipants = this asMandatory limitParticipants
            val lat = this asMandatory lat
            val long = this asMandatory long
            val date = this asMandatory date

            return Event(id, numReviews, averageGrade, numParticipants, limitParticipants, lat, long, date)
        }
    }
}