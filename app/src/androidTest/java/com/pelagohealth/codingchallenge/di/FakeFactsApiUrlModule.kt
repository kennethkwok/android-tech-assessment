package com.pelagohealth.codingchallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class],
    replaces = [FactsApiUrlModule::class])
@Module
object FakeFactsApiUrlModule {
    @Provides
    @Singleton
    @Named("BaseUrl")
    fun provideBaseUrl(): String = "http://127.0.0.1:8080"
}