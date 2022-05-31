package com.github.sdp.ratemyepfl.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.ui.adapter.ClassAdapter
import com.github.sdp.ratemyepfl.backend.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A day in the timetable
 */
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

    @Suppress("UNCHECKED_CAST")
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
            val c = courseRepo.getCourseByCourseCode(id)
            if (c != null) {
                displayReviews(
                    id,
                    c
                )
            }
        }
    }

    private fun displayRoomReviews(id: String) {
        lifecycleScope.launch {
            val r = roomRepo.getRoomByName(id)
            if (r != null) {
                displayReviews(
                    id,
                    r
                )
            }
        }
    }

    /**
     * Launches the according review activity : course or room
     */
    private fun displayReviews(id: String, r: Reviewable) {
        val intent = Intent(activity?.applicationContext, ReviewActivity::class.java)
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

