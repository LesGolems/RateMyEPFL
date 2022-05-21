package com.github.sdp.ratemyepfl.model.items

/**
 * An item that is displayed on the map
 */
interface DisplayableOnMap {
    /**
     * Creates a map item of the item to add it on the map
     */
    fun toMapItem(): MapItem
}
