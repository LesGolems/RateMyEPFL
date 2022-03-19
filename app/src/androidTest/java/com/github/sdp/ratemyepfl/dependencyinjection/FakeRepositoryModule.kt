package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.database.*
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
    abstract fun provideCoursesRepository(repo: FakeItemsRepository): ItemsRepositoryInterface

    @Singleton
    @Binds
    abstract fun provideReviewRepo(repo: FakeReviewsRepository): ReviewsRepositoryInterface

}
