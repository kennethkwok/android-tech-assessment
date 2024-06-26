package com.pelagohealth.codingchallenge.di

import com.pelagohealth.codingchallenge.repository.FactRepository
import com.pelagohealth.codingchallenge.repository.FactRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies for the repository layer.
 */
@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsFactRepository(factRepository: FactRepositoryImpl): FactRepository
} 