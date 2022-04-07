package com.github.sdp.ratemyepfl.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ImageDetailActivity
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil

class PhotoAdapter :
    ListAdapter<Int, PhotoAdapter.PhotoViewHolder>(AdapterUtil.diffCallback<Int>()) {

    private var imageIdList = mutableListOf<Int>()

    inner class PhotoViewHolder(photoView: View) :
        RecyclerView.ViewHolder(photoView) {

        private val imageView: ImageView =
            photoView.findViewById(R.id.photoImageView)

        fun bind(@DrawableRes photoId: Int) {
            imageView.setImageResource(photoId)
            imageView.tag = photoId // There is no method imageView.resourceId
            imageView.setOnClickListener {
                seeDetails(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_photo_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val id = imageIdList[position]
        holder.bind(id)
    }

    override fun getItemCount(): Int {
        return imageIdList.size
    }

    fun setData(list: MutableList<Int>?) {
        this.imageIdList = list!!
        submitList(list)
    }

    /**
     * Display the clicked photo in fullscreen
     */
    private fun seeDetails(view: View) {
        val intent = Intent(view.context, ImageDetailActivity::class.java)
        intent.putExtra(ImageDetailActivity.EXTRA_PHOTO_DISPLAYED, view.tag as Int)
        view.context.startActivity(intent)

    }
}