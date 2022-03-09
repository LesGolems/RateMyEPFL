package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.auth.UserAuth
import com.github.sdp.ratemyepfl.auth.UserAuthImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DependencyInjectionModule {

    @Singleton
    @Binds
    abstract fun provideUserAuth(userAuth: UserAuthImpl): UserAuth

}