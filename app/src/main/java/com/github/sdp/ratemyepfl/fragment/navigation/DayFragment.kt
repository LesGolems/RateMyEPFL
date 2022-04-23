package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.fragment.app.Fragment
import com.github.sdp.ratemyepfl.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : Fragment(R.layout.fragment_day) {

    enum class DAYS(val day: String) {
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Fryday");


        fun toFragment() = when (this) {
            MONDAY -> DayFragment()
            TUESDAY -> DayFragment()
            WEDNESDAY -> DayFragment()
            THURSDAY -> DayFragment()
            FRIDAY -> DayFragment()
        }
    }
}

