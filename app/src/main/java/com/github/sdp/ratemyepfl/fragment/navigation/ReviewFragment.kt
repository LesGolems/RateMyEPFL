package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.reviewTabLayout)
        viewPager = view.findViewById(R.id.reviewTabViewPager)
        val viewPagerAdapter = ReviewableViewPagerAdapter(this)

        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = ReviewableTabFragment.fromPositionToTabName(position)
        }.attach()
    }

}