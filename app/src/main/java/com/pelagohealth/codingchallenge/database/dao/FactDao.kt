package com.pelagohealth.codingchallenge.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pelagohealth.codingchallenge.database.model.FactEntity

@Dao
interface FactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FactEntity)

    @Query("select * from facts order by timestamp desc limit :number")
    suspend fun getFacts(number: Int): List<FactEntity>

    @Delete
    suspend fun deleteFact(fact: FactEntity)
}