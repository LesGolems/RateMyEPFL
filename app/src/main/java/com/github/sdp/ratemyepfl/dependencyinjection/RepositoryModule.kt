package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.*
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
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
    abstract fun provideCourseRepo(repo: CourseRepository): ItemRepository<Course>

    @Singleton
    @Binds
    abstract fun provideClassroomRepo(repo: ClassroomRepository): ItemRepository<Classroom>

    @Singleton
    @Binds
    abstract fun provideRestaurantRepo(repo: RestaurantRepository): ItemRepository<Restaurant>

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: ReviewsRepositoryImpl): ReviewsRepository

    @Singleton
    @Binds
    abstract fun provideUserRepo(repo: UserDatabase): UserRepository

    @Singleton
    @Binds
    abstract fun provideImageStorage(repo: ImageStorage): Storage<ImageFile>

}