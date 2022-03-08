package com.github.sdp.ratemyepfl.dependencyinjection

import com.github.sdp.ratemyepfl.auth.FakeUserAuth
import com.github.sdp.ratemyepfl.auth.UserAuth
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
    abstract fun provideUserAuth(userAuth : FakeUserAuth): UserAuth
}