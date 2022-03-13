package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course

class CoursesRepository (courses : Database<Course>) : Repository<Course>(courses) {

    override fun add(value: Course) {
    }

    override fun remove(value: Course) {
    }


}