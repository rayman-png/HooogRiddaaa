package com.example.mobilegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    // 1. Read the Sort Preference
    val isSortByHighScore: StateFlow<Boolean> = repository.readSortOrder()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // 2. Automatically update the list when Sort Preference changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val allScores: StateFlow<List<ScoreEntity>> = isSortByHighScore
        .flatMapLatest { isHighScore ->
            repository.getScores(isHighScore)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertRandomScore() = viewModelScope.launch {
        // Generate random score 0-100
        val randomScore = (0..100).random()
        val timestamp = System.currentTimeMillis()
        repository.insert(ScoreEntity(score = randomScore, date = timestamp))
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun toggleSortOrder() = viewModelScope.launch {
        repository.saveSortOrder(!isSortByHighScore.value)
    }
}

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}