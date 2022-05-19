package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.FragmentViewPagerAdapter
import com.github.sdp.ratemyepfl.model.items.*
import com.github.sdp.ratemyepfl.model.serializer.getReviewable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val icons = listOf(R.drawable.ic_info_white_24dp, R.drawable.ic_list_white_24dp, R.drawable.ic_camera_white_24dp)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = requireActivity().intent.getReviewable(ReviewActivity.EXTRA_ITEM_REVIEWED)
        tabLayout = view.findViewById(R.id.reviewTabLayout)
        viewPager = view.findViewById(R.id.reviewTabViewPager)
        val fragments = getFragments(item)

        viewPager.offscreenPageLimit = fragments.size
        val viewPagerAdapter = FragmentViewPagerAdapter(fragments, this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()
    }

    private fun getFragments(item : Reviewable) = when (item) {
        is Classroom -> listOf(RoomReviewInfoFragment(), ReviewListFragment(), RoomReviewPictureFragment())
        is Course -> listOf(CourseReviewInfoFragment(), ReviewListFragment())
        is Restaurant -> listOf(RestaurantReviewInfoFragment(), ReviewListFragment())
        is Event -> listOf(EventReviewInfoFragment(), ReviewListFragment())
    }
}