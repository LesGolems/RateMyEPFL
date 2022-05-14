package com.github.sdp.ratemyepfl.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ImageDetailActivity
import com.github.sdp.ratemyepfl.utils.AdapterUtil
import com.github.sdp.ratemyepfl.model.ImageFile

class RoomPictureAdapter :
    ListAdapter<ImageFile, RoomPictureAdapter.RoomPictureViewHolder>(AdapterUtil.diffCallback<ImageFile>()) {

    private var imageList = mutableListOf<ImageFile?>()

    inner class RoomPictureViewHolder(pictureView: View) :
        RecyclerView.ViewHolder(pictureView) {

        private val imageView: ImageView =
            pictureView.findViewById(R.id.pictureImageView)

        fun bind(img: ImageFile) {
            imageView.setImageBitmap(img.data)
            imageView.setOnClickListener {
                // Display the clicked picture in fullscreen
                ImageDetailActivity.pictureDisplayed = img.data
                val intent = Intent(it.context, ImageDetailActivity::class.java)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomPictureViewHolder {
        return RoomPictureViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_picture_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoomPictureViewHolder, position: Int) {
        val img = imageList[position]
        if (img != null) {
            holder.bind(img)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setData(list: MutableList<ImageFile?>) {
        this.imageList = list
        submitList(list)
    }
}