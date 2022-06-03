package com.github.sdp.ratemyepfl.model.post

import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.user.User

open class ObjectWithAuthor<T>(
    open val obj: T,
    open val author: User? = null,
    open val image: ImageFile? = null
)

typealias ReviewWithAuthor = ObjectWithAuthor<Review>
typealias SubjectWithAuthor = ObjectWithAuthor<Subject>
typealias CommentWithAuthor = ObjectWithAuthor<Comment>
typealias EventWithAuthor = ObjectWithAuthor<Event>