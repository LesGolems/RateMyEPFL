package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.review.Review
import com.google.firebase.firestore.FirebaseFirestore

class ReviewsRepository(db : FirebaseFirestore) : Repository<Review>(db) {
}