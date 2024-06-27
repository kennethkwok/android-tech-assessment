package com.pelagohealth.codingchallenge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pelagohealth.codingchallenge.database.dao.FactDao
import com.pelagohealth.codingchallenge.database.model.FactEntity

@Database(entities = [FactEntity::class], version = 1)
abstract class FactDatabase : RoomDatabase() {
    abstract fun factDao(): FactDao
}
