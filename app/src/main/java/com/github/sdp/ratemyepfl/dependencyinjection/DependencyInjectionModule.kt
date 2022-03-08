package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.auth.UserAuth
import com.github.sdp.ratemyepfl.auth.UserAuthImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DependencyInjectionModule {

    @Singleton
    @Provides
    fun provideUserAuth(userauth : UserAuthImpl): UserAuth {
        return userauth
    }

}