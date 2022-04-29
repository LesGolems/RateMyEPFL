package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ClassAdapter
import com.github.sdp.ratemyepfl.model.items.Class
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : Fragment(R.layout.fragment_day) {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val day = arguments?.get("day")?.toString()
        val timetable = arguments?.getSerializable("timetable")

        if (day == null || timetable == null) return

        recyclerView = view.findViewById(R.id.classRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ClassAdapter(day, timetable as List<Class>)
    }

    enum class DAYS(val day: String) {
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday");

        fun toFragment(day: String, timetable: ArrayList<Class>): DayFragment {
            val frag = DayFragment()
            val args = Bundle()
            args.putString("day", day)
            args.putSerializable("timetable", timetable)
            frag.arguments = args
            return frag
        }
    }
}

