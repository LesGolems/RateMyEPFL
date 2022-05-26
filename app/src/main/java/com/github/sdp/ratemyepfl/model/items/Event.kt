package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.time.Period
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val eventId: String = "",
    val name: String = "",
    val numParticipants: Int = 0,
    val limitParticipants: Int = 0,
    val participants: List<String> = listOf(),
    val creator: String = "",
    override val grade: Double = 0.0,
    override val numReviews: Int = 0,
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val period: Period = Period.DEFAULT_PERIOD
) : Reviewable(), DisplayableOnMap {

    override fun toString(): String {
        return name
    }

    fun showParticipation(): String {
        return "Participants: $numParticipants/$limitParticipants"
    }

    fun withId(id: String): Event = this.copy(eventId = id)

    override fun getId(): String = eventId

    override fun toMapItem(): MapItem {
        return EventItem(
            this,
            MapActivityUtils.PHOTO_MAPPING.getOrDefault(
                name,
                R.raw.niki
            ), // Arbitrary default value
            R.drawable.ic_calendar_solid_small
        )
    }

    /**
     * Builder to create an event step by step
     * Mandatory fields are:
     *  - [name]
     */
    data class Builder(
        private var eventId: String? = "",
        private var name: String? = null,
        private var numParticipants: Int? = 0,
        private var limitParticipants: Int? = null,
        private var participants: List<String>? = listOf(),
        private var creator: String? = null,
        private var grade: Double? = 0.0,
        private var numReviews: Int? = 0,
        private var lat: Double? = null,
        private var long: Double? = null,
        private var period: Period? = null,
    ) : ReviewableBuilder<Event> {

        fun setId(eventId: String?) = apply {
            this.eventId = eventId
        }

        fun name(name: String?) = apply {
            this.name = name
        }

        fun setCreator(creator: String?) = apply {
            this.creator = creator
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

        fun setPeriod(period: Period?) = apply {
            this.period = period
        }

        fun setGrade(grade: Double?) = apply {
            this.grade = grade
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
        }

        override fun build(): Event {
            val creator = this asMandatory creator
            val id = this asMandatory eventId
            val name = this asMandatory name
            val numParticipants = this asMandatory numParticipants
            val limitParticipants = this asMandatory limitParticipants
            val participants = this asMandatory participants
            val lat = this asMandatory lat
            val long = this asMandatory long
            val date = this asMandatory period
            val grade = this asMandatory grade
            val numReviews = this asMandatory numReviews

            return Event(
                id,
                name,
                numParticipants,
                limitParticipants,
                participants,
                creator,
                grade,
                numReviews,
                lat,
                long,
                date
            )
        }
    }
}