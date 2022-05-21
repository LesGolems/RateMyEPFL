package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.NoiseInfo
import com.github.sdp.ratemyepfl.utils.AdapterUtil
import com.github.sdp.ratemyepfl.utils.SoundDisplayUtils.decibelMap
import com.github.sdp.ratemyepfl.utils.TimeUtils.prettyTime

class RecordAdapter():
    ListAdapter<NoiseInfo, RecordAdapter.NoiseInfoViewHolder>(AdapterUtil.diffCallback<NoiseInfo>()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class NoiseInfoViewHolder(recordView: View) :
        RecyclerView.ViewHolder(recordView) {

        private val smiley: ImageView = recordView.findViewById(R.id.record_smiley)
        private val dbText: TextView = recordView.findViewById(R.id.dbRecord)
        private val dateRecord: TextView = recordView.findViewById(R.id.dateRecord)

        fun bind(noiseInfo: NoiseInfo) {
            val (text, color, emoji) = decibelMap(noiseInfo.measure)
            smiley.setImageResource(emoji)
            (text + " (${noiseInfo.measure} dB)").also { dbText.text = it }
            dbText.setTextColor(color)
            val date = prettyTime(noiseInfo.date)
            dateRecord.text = date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoiseInfoViewHolder {
        return NoiseInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.record_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoiseInfoViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

}