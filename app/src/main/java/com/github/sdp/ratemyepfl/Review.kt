package com.github.sdp.ratemyepfl

import java.time.LocalDate

abstract class Review(val rate: Int, val comment: String, val date : LocalDate) {

}
