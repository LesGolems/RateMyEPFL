package com.github.sdp.ratemyepfl.model.items

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


abstract class MapItem(
    private val position: LatLng,
    val item: Reviewable,
    val id: String,
    val name: String,
    val photo: Int,
    val icon: Int
) : ClusterItem {

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }

    fun onClickIntent(activity: FragmentActivity?): Intent {
        val intent = Intent(activity, ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, item)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, id)
        return intent
    }
}