package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.CourseDatabase
import com.github.sdp.ratemyepfl.database.FakeCoursesDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewComponent

@Module
@InstallIn(ViewComponent::class)
abstract class CourseDatabaseModule {
    @Provides
    fun providesCourseDatabase(): CourseDatabase = FakeCoursesDatabase()
}