package com.github.sdp.ratemyepfl.ui.layout

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.ImageFile
import java.io.InputStream

class LoadingImageView(
    val image: ImageView,
    progressBar: ProgressBar,
    val errorText: TextView,
    defaultImageId: Int? = null
) : LoadingView<ImageView>(image, progressBar) {

    val defaultImage: ImageFile? = defaultImageId?.let {
        val context = view.context
        val inputStream: InputStream = context.resources.openRawResource(it)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        ImageFile("default", bitmap)
    }

    /**
     * Should be use if the layout contains [R.layout.layout_animated_circle_image_view].
     *
     * @param parent: The id of the layout included
     * @param defaultImageId: The id of a default image from a raw resource.
     */
    constructor(parent: View, defaultImageId: Int?) : this(
        parent.findViewById(R.id.loadingImageView),
        parent.findViewById(R.id.loadingImageProgressBar),
        parent.findViewById(R.id.loadingImageErrorText),
        defaultImageId
    )

    init {
        setDefaultImage()
    }

    fun setDefaultImage() = defaultImage?.run {
        image.setImageBitmap(this.data)
    }

}