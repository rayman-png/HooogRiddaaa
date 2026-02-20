package com.example.mobilegame

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(private val scoreDao: ScoreDAO, private val datastore: DataStore<Preferences>) {

    // Helper key for DataStore
    private val IS_SORT_BY_HIGHSCORE = booleanPreferencesKey("is_sort_by_highscore")

    // Returns flow based on the requested sort order
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

    // Save Sort Preference
    suspend fun saveSortOrder(isHighScore: Boolean) {
        datastore.edit { preferences ->
            preferences[IS_SORT_BY_HIGHSCORE] = isHighScore
        }
    }

    // Read Sort Preference (Default to false/Date)
    fun readSortOrder(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[IS_SORT_BY_HIGHSCORE] ?: false
        }
    }
}