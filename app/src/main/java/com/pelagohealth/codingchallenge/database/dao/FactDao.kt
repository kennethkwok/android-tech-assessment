package com.pelagohealth.codingchallenge.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pelagohealth.codingchallenge.database.model.FactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FactEntity)

    @Query("select * from facts order by timestamp desc limit :number")
    fun getFacts(number: Int): Flow<List<FactEntity>>

    @Query("delete from facts where id = :id")
    suspend fun deleteFact(id: String)
}
