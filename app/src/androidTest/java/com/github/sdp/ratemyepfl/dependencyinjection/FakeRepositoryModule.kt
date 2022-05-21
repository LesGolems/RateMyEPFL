package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.backend.database.*
import com.github.sdp.ratemyepfl.backend.database.fakes.*
import com.github.sdp.ratemyepfl.backend.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.RestaurantRepository
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
    abstract fun provideCourseRepo(repo: FakeCourseRepository): CourseRepository

    @Singleton
    @Binds
    abstract fun provideClassroomRepo(repo: FakeClassroomRepository): ClassroomRepository

    @Singleton
    @Binds
    abstract fun provideRestaurantRepo(repo: FakeRestaurantRepository): RestaurantRepository

    @Singleton
    @Binds
    abstract fun provideEventRepo(repo: FakeEventRepository): EventRepository

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: FakeReviewRepository): ReviewRepository

    @Singleton
    @Binds
    abstract fun provideGradeInfoRepo(repo: FakeGradeInfoRepository): GradeInfoRepository

    @Singleton
    @Binds
    abstract fun provideRoomNoiseRepo(repo: FakeRoomNoiseRepository): RoomNoiseRepository

    @Singleton
    @Binds
    abstract fun provideUserRepo(repo: FakeUserRepository): UserRepository

    @Singleton
    @Binds
    abstract fun provideImageRepo(repo: FakeImageStorage): Storage<ImageFile>
}
