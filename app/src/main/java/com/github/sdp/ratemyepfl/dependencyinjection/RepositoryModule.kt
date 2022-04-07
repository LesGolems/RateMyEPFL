package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.*
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
    abstract fun provideCourseRepo(repo: CourseRepository): CourseRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideClassroomRepo(repo: ClassroomRepository): ClassroomRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideRestaurantRepo(repo: RestaurantRepository): RestaurantRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: ReviewRepository): ReviewRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideUserRepo(repo: UserDatabase): UserRepository

    @Singleton
    @Binds
    abstract fun provideImageStorage(repo: ImageStorage): Storage<ImageFile>

    @Singleton
    @Binds
    abstract fun providePictureRepo(repo: PictureRepository): PictureRepositoryInterface
}