package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Singleton
    @Binds
    abstract fun provideCoursesRepository(repo: FakeCoursesRepository): CoursesRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideClassroomsRepository(repo: FakeClassroomsRepository): ClassroomsRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideCourseReviewRepo(repo: FakeCoursesReviewsRepository): CoursesReviewsRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideClassroomReviewRepo(repo: FakeClassroomsReviewsRepository): ClassroomsReviewsRepositoryInterface

}
