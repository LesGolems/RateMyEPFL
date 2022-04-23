package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimetableFragment : Fragment(R.layout.fragment_timetable){
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}