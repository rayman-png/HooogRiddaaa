package com.example.mobilegame

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDAO {

    // Sort by Date Descending (Newest first)
    @Query("SELECT * FROM score_table ORDER BY date DESC")
    fun getAllScoresByDate(): Flow<List<ScoreEntity>>

    // Sort by Score Descending (Highest score first)
    @Query("SELECT * FROM score_table ORDER BY score DESC")
    fun getAllScoresByHighScore(): Flow<List<ScoreEntity>>

    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("DELETE FROM score_table")
    suspend fun deleteAll()
}