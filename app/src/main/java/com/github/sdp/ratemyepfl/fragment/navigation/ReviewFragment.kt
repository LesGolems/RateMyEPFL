package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.github.sdp.ratemyepfl.R
import com.google.android.material.tabs.TabLayout

private typealias TabHandler = (TabLayout.Tab?) -> Unit

class ReviewFragment : Fragment(R.layout.fragment_review) {
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.reviewTabLayout)

        setupTabNavigation()

    }

    private fun setupTabNavigation() {
        // Initially set the corresponding tab
        childFragmentManager.commit {
            val fragment = fromTabToFragment(tabLayout.selectedTabPosition)
            if (fragment != null) {
                add(
                    R.id.reviewTabFragment,
                    fragment
                )
                setReorderingAllowed(true)
                addToBackStack("tab")
            }
        }

        tabLayout.addOnTabSelectedListener(tabManager)
    }

    private val tabManager =
        onTabSelectedListener(
            { tab ->
                when (tab?.position) {
                    0 -> changeTab<CourseTabFragment>()
                    1 -> changeTab<ClassroomTabFragment>()
                    2 -> {}
                }
            },
            {},
            {})

    private inline fun<reified T : Fragment> changeTab() {
        childFragmentManager.commit {
            replace<T>(R.id.reviewTabFragment)
            setReorderingAllowed(true)
            addToBackStack("tab")
        }
    }


    companion object {
        private fun fromTabToFragment(tabPosition: Int): Fragment? =
            when (tabPosition) {
                0 -> CourseTabFragment()
                1 -> ClassroomTabFragment()
                else -> null
            }

        private fun onTabSelectedListener(
            tabSelectedHandler: TabHandler,
            tabReselectedHandler: TabHandler,
            tabUnselectedHandler: TabHandler
        ): TabLayout.OnTabSelectedListener =
            object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tabSelectedHandler(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    tabReselectedHandler(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tabUnselectedHandler(tab)
                }
            }
    }


}