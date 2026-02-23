package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DisplayHighScore(username: String) {
    val context = LocalContext.current
    val database = ScoreDatabase.getDatabase(context)
    val repository = Repository(database.scoreDao(), context.dataStore)

    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(repository)
    )

    LaunchedEffect(username) {
        viewModel.setCurrentUser(username)
    }
    val allScores by viewModel.allScores.collectAsState()
    val currentSortType by viewModel.sortType.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Control Row for Sorting Choices
        Text(text = "Sort By:", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SortOptionButton("Score", SortType.SCORE, currentSortType) { viewModel.changeSortType(it) }
            SortOptionButton("Date", SortType.DATE, currentSortType) { viewModel.changeSortType(it) }
            SortOptionButton("Name", SortType.NAME, currentSortType) { viewModel.changeSortType(it) }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Leaderboard", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.deleteAll() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Clear")
            }
        }
        HorizontalDivider(thickness = 2.dp)

        Spacer(modifier = Modifier.height(8.dp))

        if (allScores.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "No scores yet!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(allScores) { scoreItem ->
                    ScoreItemRow(scoreItem)
                }
            }
        }
    }
}

@Composable
fun ScoreItemRow(item: ScoreEntity) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(item.date))

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Score: ${item.score}", style = MaterialTheme.typography.titleLarge)
                Text(text = dateString, style = MaterialTheme.typography.bodyMedium)
            }
            // Add this line to see the username associated with the score
            Text(text = "Player: ${item.username}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SortOptionButton(
    label: String,
    type: SortType,
    currentType: SortType,
    onSelect: (SortType) -> Unit
) {
    FilterChip(
        selected = currentType == type,
        onClick = { onSelect(type) },
        label = { Text(label) }
    )
}