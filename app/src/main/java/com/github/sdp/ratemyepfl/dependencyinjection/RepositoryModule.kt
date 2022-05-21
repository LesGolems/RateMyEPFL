package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.backend.database.*
import com.github.sdp.ratemyepfl.backend.database.firebase.*
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.reviewable.*
import com.github.sdp.ratemyepfl.model.ImageFile
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideCourseRepo(repo: CourseRepositoryImpl): CourseRepository

    @Singleton
    @Binds
    abstract fun provideClassroomRepo(repo: ClassroomRepositoryImpl): ClassroomRepository

    @Singleton
    @Binds
    abstract fun provideRestaurantRepo(repo: RestaurantRepositoryImpl): RestaurantRepository

    @Singleton
    @Binds
    abstract fun provideEventRepo(repo: EventRepositoryImpl): EventRepository

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: ReviewRepositoryImpl): ReviewRepository

    @Singleton
    @Binds
    abstract fun provideUserRepo(repo: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun provideGradeInfoRepo(repo: GradeInfoRepositoryImpl): GradeInfoRepository

    @Singleton
    @Binds
    abstract fun provideRoomNoiseRepo(repo: RoomNoiseRepositoryImpl): RoomNoiseRepository

    @Singleton
    @Binds
    abstract fun provideImageStorage(repo: FirebaseImageStorage): Storage<ImageFile>
}