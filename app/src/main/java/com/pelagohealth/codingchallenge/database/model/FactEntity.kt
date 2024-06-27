package com.pelagohealth.codingchallenge.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facts")
data class FactEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "factText") val text: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
)
