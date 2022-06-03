package com.github.sdp.ratemyepfl.model

import kotlinx.serialization.Serializable

@Serializable
data class NoiseInfo(val date: String = "", val measure: Int = 0)