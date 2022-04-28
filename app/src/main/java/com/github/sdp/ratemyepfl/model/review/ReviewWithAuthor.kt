package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User

class ReviewWithAuthor(
    val review: Review,
    val author: User? = null,
    val image: ImageFile? = null
)