package com.github.sdp.ratemyepfl.dependencyinjection

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [EmulatorModule::class]
)
object FakeEmulatorModule {

    @Singleton
    @Provides
    fun providesFirestore(): FirebaseFirestore {
        val firestore = Firebase.firestore
        firestore.useEmulator("10.0.2.2", 8080)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        firestore.firestoreSettings = settings
        return firestore
    }

}