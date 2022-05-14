package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.ClassAdapter
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DayFragment : Fragment(R.layout.fragment_day) {

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var courseRepo: CourseRepository

    @Inject
    lateinit var roomRepo: ClassroomRepository

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
        recyclerView.adapter = ClassAdapter(timetable as List<Class>,
            { id -> displayCourseReviews(id) },
            { id -> displayRoomReviews(id) }
        )
    }

    private fun displayCourseReviews(id: String) {
        lifecycleScope.launch {
            val c = courseRepo.getCourseById(id)
            Log.d("finfi", c.toString())
            if (c != null) {
                displayReviews(
                    R.menu.bottom_navigation_menu_course_review,
                    R.navigation.nav_graph_course_review,
                    id,
                    c
                )
            }
        }
    }

    private fun displayRoomReviews(id: String) {
        lifecycleScope.launch {
            val r = roomRepo.getRoomById(id)
            Log.d("finfi", r.toString())
            if (r != null) {
                displayReviews(
                    R.menu.bottom_navigation_menu_room_review,
                    R.navigation.nav_graph_room_review,
                    id,
                    r
                )
            }
        }
    }

    private fun displayReviews(bottomNavId: Int, graphNavId: Int, id: String, r: Reviewable) {
        val intent = Intent(activity?.applicationContext, ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, bottomNavId)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, graphNavId)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, id)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, r)
        startActivity(intent)
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

