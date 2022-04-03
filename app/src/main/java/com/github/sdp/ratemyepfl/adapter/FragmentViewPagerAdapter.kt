package com.github.sdp.ratemyepfl.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.github.sdp.ratemyepfl.fragment.navigation.ReviewableTabFragment

class FragmentViewPagerAdapter(private val fragments: List<Fragment>, fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments[position]

}