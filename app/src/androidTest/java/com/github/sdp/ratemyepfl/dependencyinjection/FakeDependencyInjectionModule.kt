package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.auth.*
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DependencyInjectionModule::class]
)
abstract class FakeDependencyInjectionModule {

    @Singleton
    @Binds
    abstract fun provideConnectedUser(user: FakeConnectedUser): ConnectedUser
}