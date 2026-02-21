package com.example.mobilegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import com.example.mobilegame.ui.theme.MobileGameTheme

data object Home
@Serializable
data class MainMenu(val username: String)
@Serializable
data class GameHome(val username: String)
@Serializable
data class GameSession(val username: String, val category: String)
@Serializable
data class HighScores(val username: String)
@Serializable
data class LogIn(val ID: String, val password: String)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileGameTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // Standard Surface doesn't require experimental annotations
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shadowElevation = 4.dp
                        ) {
                        }
                    },
                ) { innerPadding ->
                    NavLogic(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavLogic(modifier: Modifier = Modifier) {
    val backstack = remember { mutableStateListOf<Any>(Home) }

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    HomeScreen(goNext = { id -> backstack.add(MainMenu(id)) }, modifier)
                }
                is MainMenu -> NavEntry(key) {
                    MainMenuScreen(
                        username = key.username,
                        onGameMenu = { backstack.add(GameHome(key.username)) },
                        onLogout = { backstack.removeLastOrNull() },
                        onShowScores = { backstack.add(HighScores(username = key.username)) }
                    )
                }
                is HighScores -> NavEntry(key) {
                    DisplayHighScore(username = key.username) // Added this
                }
                is GameHome -> NavEntry(key) {
                    CategoryScreen(onCategorySelected = { category ->
                        backstack.add(GameSession(key.username, category))
                    })
                }
                is GameSession -> NavEntry(key) {
                    GameScreen(username = key.username, category = key.category)
                }
                else -> NavEntry(key) { Text(text = "Unknown") }
            }
        }
    )
}