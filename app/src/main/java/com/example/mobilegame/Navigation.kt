package com.example.mobilegame

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data class MainMenu(val username: String)

@Serializable
data class GameFlowRoute(val username: String, val mode: String)

@Serializable
data class Tutorial (val username: String)


@Serializable
data class HighScores(val username: String)

@Serializable
data class LogIn(val ID: String, val password: String)

@Serializable
data class GameModeSelection(val username: String)

@Composable
fun NavLogic(modifier: Modifier = Modifier) {
    val backstack = remember { mutableStateListOf<Any>(Home) }

    NavDisplay(
        backStack = backstack,
        onBack = {
            backstack.removeLastOrNull()
        },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    HomeScreen(
                        goNext = { id -> backstack.add(MainMenu(id)) },
                        modifier
                    )
                }
                is MainMenu -> NavEntry(key) {
                    MainMenuScreen(
                        username = key.username,
                        onGameMenu = { backstack.add(GameModeSelection(key.username)) },
                        onTutorial = { backstack.add(Tutorial(key.username)) },
                        onShowScores = { backstack.add(HighScores(username = key.username)) },
                        onLogout = { backstack.removeLastOrNull() }
                    )
                }
                is GameModeSelection -> NavEntry(key) {
                    GameModeSelectionScreen(
                        onModeSelected = { selectedMode ->
                            backstack.add(GameFlowRoute(key.username, mode = selectedMode))
                        },
                        onBack = { backstack.removeLastOrNull() }
                    )
                }
                is HighScores -> NavEntry(key) {
                    DisplayHighScore(username = key.username)
                }

                is Tutorial -> NavEntry(key){
                    TutorialScreen(
                        username = key.username,
                        onBack = { backstack.removeLastOrNull() }
                    )
                }

                is GameFlowRoute -> NavEntry(key) {
                    GameFlowManager(
                        username = key.username,
                        gameMode = key.mode,
                        onGameFinished = { backstack.removeLastOrNull() }
                    )
                }
                else -> NavEntry(key) {
                    Text(text = "Unknown")
                }
            }
        }
    )
}
