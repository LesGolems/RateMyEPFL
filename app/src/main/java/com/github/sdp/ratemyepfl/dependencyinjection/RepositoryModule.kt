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
    abstract fun provideItemRepo(repo: ItemsRepository): ItemsRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: ReviewsRepository): ReviewsRepositoryInterface

}