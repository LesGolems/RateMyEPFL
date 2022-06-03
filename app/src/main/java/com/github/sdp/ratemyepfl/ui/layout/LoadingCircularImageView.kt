package com.github.sdp.ratemyepfl.ui.layout

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ProgressBar
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.ImageFile
import de.hdodenhof.circleimageview.CircleImageView
import java.io.InputStream

class LoadingCircleImageView(
    val image: CircleImageView,
    progressBar: ProgressBar,
    private val defaultImageId: Int
) : LoadingView<CircleImageView>(image, progressBar) {

    /**
     * Should be use if the layout contains [R.layout.layout_animated_circle_image_view].
     *
     * @param parent: The id of the layout included
     * @param defaultImageId: The id of a default image from a raw resource.
     */
    constructor(parent: View, defaultImageId: Int) : this(
        parent.findViewById(R.id.loadingCircleImageView),
        parent.findViewById(R.id.loadingCircleImageProgressBar),
        defaultImageId
    )

    constructor(parent: View) : this(parent, R.raw.blank_profile_picture)

    init {
        image.setImageBitmap(getDefaultImage().data)
    }

    fun getDefaultImage(): ImageFile {
        val context = view.context
        val inputStream: InputStream = context.resources.openRawResource(defaultImageId)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return ImageFile("default", bitmap)
    }
}