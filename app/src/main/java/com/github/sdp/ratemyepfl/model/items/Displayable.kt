package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

/**
 * An item that is displayed on the map
 */
interface Displayable {
    /**
     * Creates a map item of the item to add it on the map
     */
    fun toMapItem(): MapItem
}