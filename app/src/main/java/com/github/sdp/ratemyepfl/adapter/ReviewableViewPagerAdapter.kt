package com.github.sdp.ratemyepfl.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.sdp.ratemyepfl.fragment.navigation.ReviewableTabFragment

class ReviewableViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = ReviewableTabFragment.NUMBER_OF_TABS

    override fun createFragment(position: Int): Fragment =
        ReviewableTabFragment.fromPositionToFragment(position)

}