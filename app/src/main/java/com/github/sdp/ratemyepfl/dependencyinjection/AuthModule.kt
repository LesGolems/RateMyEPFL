package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.ConnectedUserImpl
import com.github.sdp.ratemyepfl.auth.GoogleAuthenticator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Singleton
    @Binds
    abstract fun provideConnectedUser(user: ConnectedUserImpl): ConnectedUser

}