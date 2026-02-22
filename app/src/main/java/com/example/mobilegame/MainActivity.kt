package com.example.mobilegame

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.mobilegame.ui.theme.MobileGameTheme
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import kotlinx.serialization.Serializable
import java.io.File

data object Home
@Serializable
data class MainMenu(val username: String)
@Serializable
data class GameFlowRoute(val username: String, val mode: String)
@Serializable
data class HighScores(val username: String)
@Serializable
data class LogIn(val ID: String, val password: String)

@Serializable
data class GameModeSelection(val username:String)

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

@Composable
fun LevelTransitionScreen(
    nextCategory: String,
    allCategories: List<String>,
    onTransitionFinished: () -> Unit
) {
    var displayedCategory by remember { mutableStateOf(allCategories.random()) }

    LaunchedEffect(nextCategory) {
        for (i in 1..20) {
            displayedCategory = allCategories.random()
            delay(40L + (i * 10L))
        }
        displayedCategory = nextCategory
        delay(2000L)
        onTransitionFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Level Clear! 🎉",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Rolling next category...",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(100.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedCategory,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
@Composable
fun GameFlowManager(
    username: String,
    gameMode: String,
    onGameFinished: () -> Unit
) {
    var currentLevel by remember { mutableStateOf(0) }
    var currentCategory by remember { mutableStateOf("") }
    var playedCategories by remember { mutableStateOf(setOf<String>()) }

    var isTransitioning by remember { mutableStateOf(false) }
    var upcomingCategory by remember { mutableStateOf("") }

    val allCategories = listOf("Nature", "Kitchen", "Office", "Pets")

    if (isTransitioning) {
        LevelTransitionScreen(
            nextCategory = upcomingCategory,
            allCategories = allCategories,
            onTransitionFinished = {
                currentCategory = upcomingCategory
                playedCategories = playedCategories + upcomingCategory
                currentLevel++
                isTransitioning = false
            }
        )
    } else {
        when (currentLevel) {
            0 -> {
                CategoryScreen(
                    onCategorySelected = { selected ->
                        currentCategory = selected
                        playedCategories = setOf(selected)
                        currentLevel = 1
                    }
                )
            }
            1, 2, 3 -> {
                // Determine which screen to show based on gameMode
                if (gameMode == "Category Shuffle") {
                    CategoryShuffleScreen(
                        username = username,
                        category = currentCategory,
                        level = currentLevel,
                        onLevelComplete = {
                            if (currentLevel == 3) {
                                onGameFinished()
                            } else {
                                val remainingCategories = allCategories - playedCategories
                                upcomingCategory = remainingCategories.random()
                                isTransitioning = true
                            }
                        }
                    )
                } else {
                    // This handles the "Mystery Hunt" choice
                    MysteryHuntScreen(
                        username = username,
                        category = currentCategory,
                        level = currentLevel,
                        onLevelComplete = {
                            if (currentLevel == 3) {
                                onGameFinished()
                            } else {
                                currentLevel++
                            }
                        }
                    )
                }
            }
        }
    }
}