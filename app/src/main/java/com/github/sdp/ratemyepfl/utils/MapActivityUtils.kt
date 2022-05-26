package com.github.sdp.ratemyepfl.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object MapActivityUtils {
    val PHOTO_MAPPING = mapOf(
        "Arcadie" to R.raw.arcadie,
        "Epicure" to R.raw.epicure,
        "Niki" to R.raw.niki,
        "Roulotte du Soleil" to R.raw.soleil_roulotte,
        "Balélec" to R.raw.balelec,
        "Vivapoly" to R.raw.vivapoly
    )

    /**
     * Create a bitmap descriptor from a vector drawable
     */
    fun getMarkerIconFromDrawable(res: Resources, drawableId: Int): BitmapDescriptor? {
        val drawable = ResourcesCompat.getDrawable(res, drawableId, null)
        drawable?.let {
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            canvas.setBitmap(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)

            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
        return null
    }
}