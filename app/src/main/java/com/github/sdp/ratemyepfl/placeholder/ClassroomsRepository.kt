package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Classroom

class ClassroomsRepository (classrooms : Database<Classroom>): Repository<Classroom>(classrooms){

    override fun add(value: Classroom) {
    }

    override fun remove(value: Classroom) {
    }


}