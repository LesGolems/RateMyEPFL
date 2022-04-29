package com.github.sdp.ratemyepfl.model

import android.graphics.Bitmap

data class ImageFile(
    val id: String,
    val data: Bitmap
) {
    /**
     * The size of the image in bytes
     */
    val size = data.byteCount
}