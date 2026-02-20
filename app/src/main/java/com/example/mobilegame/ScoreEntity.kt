package com.example.mobilegame

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val date: Long // Store as timestamp
)