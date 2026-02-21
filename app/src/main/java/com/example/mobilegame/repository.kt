package com.example.mobilegame

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "score_prefs")

class Repository(private val scoreDao: ScoreDAO, private val datastore: DataStore<Preferences>) {

    private val IS_SORT_BY_HIGHSCORE = booleanPreferencesKey("is_sort_by_highscore")

    fun getScores(sortByHighScore: Boolean): Flow<List<ScoreEntity>> {
        return if (sortByHighScore) {
            scoreDao.getAllScoresByHighScore()
        } else {
            scoreDao.getAllScoresByDate()
        }
    }

    suspend fun insert(score: ScoreEntity) {
        scoreDao.insert(score)
    }

    suspend fun deleteAll() {
        scoreDao.deleteAll()
    }

    suspend fun saveSortOrder(isHighScore: Boolean) {
        datastore.edit { preferences ->
            preferences[IS_SORT_BY_HIGHSCORE] = isHighScore
        }
    }

    fun readSortOrder(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[IS_SORT_BY_HIGHSCORE] ?: false
        }
    }

    fun getScoresByName(): Flow<List<ScoreEntity>> {
        return scoreDao.getAllScoresByName()
    }

    fun getScoresFlow(): Flow<List<ScoreEntity>> {
        return scoreDao.getAllScores()
    }
}