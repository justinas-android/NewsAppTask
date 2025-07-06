package com.example.newsapp.news.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsDetailsScreen(
    author: String,
    title: String,
    description: String,
    url: String,
    urlToImage: String,
    publishedAt: String
) {
    Column {
        Text(text = title)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = description)
    }

}
