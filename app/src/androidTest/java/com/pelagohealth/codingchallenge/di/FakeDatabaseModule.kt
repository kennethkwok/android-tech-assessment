package com.pelagohealth.codingchallenge.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.pelagohealth.codingchallenge.database.FactDatabase
import com.pelagohealth.codingchallenge.database.dao.FactDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class],
    replaces = [DatabaseModule::class])
@Module
object FakeDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): FactDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            FactDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideDao(factDatabase: FactDatabase) : FactDao{
        return factDatabase.factDao()
    }
}