package com.pelagohealth.codingchallenge.di

import android.content.Context
import androidx.room.Room
import com.pelagohealth.codingchallenge.database.FactDatabase
import com.pelagohealth.codingchallenge.database.dao.FactDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies for the database
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): FactDatabase {
        return Room.databaseBuilder(
            appContext,
            FactDatabase::class.java,
            "fact_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(factDatabase: FactDatabase) : FactDao{
        return factDatabase.factDao()
    }
}