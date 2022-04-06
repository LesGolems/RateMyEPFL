package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.*
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
    abstract fun provideCourseRepo(repo: CourseRepository): CourseRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideClassroomRepo(repo: ClassroomRepository): ClassroomRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideRestaurantRepo(repo: RestaurantRepository): RestaurantRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: ReviewsRepositoryImpl): ReviewsRepository

    @Singleton
    @Binds
    abstract fun providePictureRepo(repo: PictureRepository): PictureRepositoryInterface

}