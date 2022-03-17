package com.github.sdp.ratemyepfl.dependencyinjection

import android.app.Application
import com.github.sdp.ratemyepfl.database.CourseDatabase
import com.github.sdp.ratemyepfl.database.FakeCoursesDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CourseDatabaseModule {
    @Binds
    abstract fun bindsCourseDatabase(db: FakeCoursesDatabase): CourseDatabase
}