package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.FragmentViewPagerAdapter
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class TimetableFragment : Fragment(R.layout.fragment_timetable) {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.timetableTabLayout)
        viewPager = view.findViewById(R.id.timetableTabViewPager)

        viewModel.timetable.observe(viewLifecycleOwner) {
            it?.let {
                createFragments(it)
            }
        }
    }

    private fun createFragments(timetable : ArrayList<Class>){
        if (timetable.isNotEmpty()) {
            val fragments = DayFragment.DAYS.values()
                .map {
                    val classes =
                        timetable.filter { c -> c.day == it.ordinal }.toCollection(ArrayList())
                    it.toFragment(it.day, classes)
                }
                .toList()

            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2
            viewPager.currentItem = if (today in 0..4) today else 0

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

}