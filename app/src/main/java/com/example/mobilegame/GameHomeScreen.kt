package com.example.mobilegame

import ads_mobile_sdk.h4
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@Composable
fun CategoryScreen(
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Nature", "Kitchen", "Office", "Pets")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select a Category To Start With", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(24.dp))

        categories.forEach { category ->
            Button(
                onClick = {
                    onCategorySelected(category)
                },
                modifier = Modifier.fillMaxWidth(0.7f).padding(8.dp)
            ) {
                Text(category)
            }
        }
    }
}