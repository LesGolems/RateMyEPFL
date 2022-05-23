package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.ui.adapter.FragmentViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewableFragment : Fragment(R.layout.fragment_review) {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.reviewTabLayout)
        viewPager = view.findViewById(R.id.reviewTabViewPager)
        val fragments = ReviewableTabFragment.TAB.values()
            .map { it.toFragment() }
            .toList()

        viewPager.offscreenPageLimit = fragments.size
        val viewPagerAdapter = FragmentViewPagerAdapter(fragments, this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = ReviewableTabFragment.TAB
                .values()[position]
                .tabName
        }.attach()
    }

}