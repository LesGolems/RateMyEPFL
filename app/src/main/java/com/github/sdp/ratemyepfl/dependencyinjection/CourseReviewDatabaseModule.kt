package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.CourseReviewDatabase
import com.github.sdp.ratemyepfl.database.FakeCourseReviewDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CourseReviewDatabaseModule {

    @Provides
    fun providesCourseReviewDatabase(): CourseReviewDatabase = FakeCourseReviewDatabase()
}