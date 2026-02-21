package com.example.mobilegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SortType { SCORE, DATE, NAME }

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.SCORE)
    val sortType = _sortType.asStateFlow()
    val allScores: StateFlow<List<ScoreEntity>> = repository.getScoresFlow()
        .combine(_sortType) { scores, type ->
            when (type) {
                SortType.SCORE -> scores.sortedByDescending { it.score }
                SortType.DATE -> scores.sortedByDescending { it.date }
                SortType.NAME -> scores.sortedWith(
                    compareBy<ScoreEntity> { it.username.lowercase() }
                        .thenByDescending { it.score }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentUser = MutableStateFlow("Guest")
    val currentUser = _currentUser.asStateFlow()

    fun setCurrentUser(username: String) {
        _currentUser.value = username
    }

    fun changeSortType(newType: SortType) {
        _sortType.value = newType
    }

    fun insertDebugScore() = viewModelScope.launch {
        val randomScore = (0..100).random()
        val thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000
        val randomPastTime = (0..thirtyDaysInMillis).random().toLong()
        val randomTimestamp = System.currentTimeMillis() - randomPastTime
        val user = _currentUser.value
        repository.insert(ScoreEntity(score = randomScore, date = randomTimestamp, username = user))
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
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