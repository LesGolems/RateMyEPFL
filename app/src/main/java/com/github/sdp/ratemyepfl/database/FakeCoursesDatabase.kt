package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate
import javax.inject.Inject

class FakeCoursesDatabase : CourseDatabase {
    override fun getReviews(): List<CourseReview> {
        return listOf(
            CourseReview.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setDate(LocalDate.now())
                .build(),
            CourseReview.Builder().setTitle("SA PARLE CASH")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setDate(LocalDate.now())
                .build(),
            CourseReview.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setDate(LocalDate.now())
                .build(),
            CourseReview.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setDate(LocalDate.now())
                .build()
        )
    }

    override fun getCourses(): List<Course> {
        return listOf(
            Course(
                name="Software development project",
                faculty="IC",
                teacher="George Candea",
                credits=4,
                courseCode="CS-306"
            ),
            Course(
                name="Calcul quantique",
                faculty="IC",
                teacher="Nicolas Macris",
                credits=4,
                courseCode="CS-308"
            ),
            Course(
                name="Intelligence artificielle",
                faculty="IC",
                teacher="Boi Faltings",
                credits=4,
                courseCode="CS-330"
            ),
            Course(
                name="Projet de systems-on-chip",
                faculty="IC",
                teacher="René Beuchat",
                credits=3,
                courseCode="CS-309"
            ),
            Course(
                name="Introduction to database systems",
                faculty="IC",
                teacher="Christoph Koch",
                credits=4,
                courseCode="CS-332"
            )
        )
    }
}