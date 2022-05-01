package com.github.sdp.ratemyepfl.model.items

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


abstract class MapItem(
    private val position: LatLng,
    val name: String,
    val photo: Int,
    val icon: BitmapDescriptor?
) : ClusterItem {
    protected abstract val layout: Int
    protected abstract val graph: Int

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
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, name)
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, layout)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, graph)
        return intent
    }
}