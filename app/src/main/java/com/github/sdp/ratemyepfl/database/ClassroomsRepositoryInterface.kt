package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom

interface ClassroomsRepositoryInterface {
    suspend fun get() : List<Classroom?>
}