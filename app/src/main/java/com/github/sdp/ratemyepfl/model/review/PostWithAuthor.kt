package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User

open class PostWithAuthor<T : Post>(
    open val post: T,
    open val author: User? = null,
    open val image: ImageFile? = null
)

typealias ReviewWithAuthor = PostWithAuthor<Review>
typealias SubjectWithAuthor = PostWithAuthor<Subject>