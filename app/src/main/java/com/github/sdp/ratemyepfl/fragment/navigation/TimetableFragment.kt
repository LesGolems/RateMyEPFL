package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.FragmentViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimetableFragment : Fragment(R.layout.fragment_timetable) {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.timetableTabLayout)
        viewPager = view.findViewById(R.id.timetableTabViewPager)
        val fragments = DayFragment.DAYS.values()
            .map{ it.toFragment()}
            .toList()

        viewPager.offscreenPageLimit = fragments.size
        val viewPagerAdapter = FragmentViewPagerAdapter(fragments, this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = DayFragment.DAYS
                .values()[position]
                .day
        }.attach()
    }

}