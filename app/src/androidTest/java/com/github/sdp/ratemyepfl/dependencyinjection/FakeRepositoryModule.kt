package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.*
import com.github.sdp.ratemyepfl.database.fakes.*
import com.github.sdp.ratemyepfl.model.ImageFile
import dagger.Binds
import dagger.Module
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
    abstract fun provideCourseRepo(repo: FakeCourseRepository): CourseRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideClassroomRepo(repo: FakeClassroomRepository): ClassroomRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideRestaurantRepo(repo: FakeRestaurantRepository): RestaurantRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideEventRepo(repo: FakeEventRepository): EventRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: FakeReviewsRepository): ReviewRepositoryInterface


    @Singleton
    @Binds
    abstract fun provideUserRepo(repo: FakeUserRepository): UserRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideImageRepo(repo: FakeImageStorage): Storage<ImageFile>
}
