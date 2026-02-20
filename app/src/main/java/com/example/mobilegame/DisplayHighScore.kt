package com.example.mobilegame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DisplayHighScore(viewModel: MainViewModel = viewModel()) {
    val allScores by viewModel.allScores.collectAsState()
    val isSortByHighScore by viewModel.isSortByHighScore.collectAsState()

    Column(modifier = Modifier.padding(16.dp).fillMaxHeight()) {

        // Control Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(text = if (isSortByHighScore) "Sorting: High Score" else "Sorting: Date")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isSortByHighScore,
                onCheckedChange = { viewModel.toggleSortOrder() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.deleteAll() }) {
                Text("Clear")
            }
        }

        HorizontalDivider()

        // List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(allScores) { scoreItem ->
                ScoreItemRow(scoreItem)
            }
        }
    }
}

@Composable
fun ScoreItemRow(item: ScoreEntity) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(item.date))

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Score: ${item.score}", style = MaterialTheme.typography.titleMedium)
            Text(text = dateString, style = MaterialTheme.typography.bodySmall)
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
    }
}