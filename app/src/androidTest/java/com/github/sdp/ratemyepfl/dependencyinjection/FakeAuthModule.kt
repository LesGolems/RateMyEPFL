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
    replaces = [AuthModule::class]
)
abstract class FakeAuthModule {

    @Singleton
    @Binds
    abstract fun provideConnectedUser(user: FakeConnectedUser): ConnectedUser

    @Singleton
    @Binds
    abstract fun provideAuthenticator(auth: FakeAuthenticator): Authenticator
}