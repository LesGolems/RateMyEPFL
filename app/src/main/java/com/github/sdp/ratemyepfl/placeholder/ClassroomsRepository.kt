package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Classroom

class ClassroomsRepository: Repository<Classroom>() {

    override suspend fun add(value: Classroom) {
    }

    override suspend fun remove(value: Classroom) {
    }

    override suspend fun get() : Set<Classroom>{
        return setOf<Classroom>()
    }


}