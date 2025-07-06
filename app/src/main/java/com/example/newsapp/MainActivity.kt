package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.news.details.NewsDetailsScreen
import com.example.newsapp.news.list.NewsMainListScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.navigation.Screen
import com.example.newsapp.utils.navigation.navigateTo
import com.example.newsapp.utils.navigation.screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                MainFlow()
            }
        }
    }
}

@Composable
fun MainFlow(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (!navController.popBackStack()) {
//                            onExitApp()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Icon Back"
                    )
                }
            }
        },
        bottomBar = {}
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.NewsMainListScreen.route
        ) {
            screen(
                screen = Screen.NewsMainListScreen
            ) {
                NewsMainListScreen(
                    onArticleClicked = { article ->
                        val route = Screen.NewsDetailsScreen.createRoute(
                            author = article.author,
                            title = article.title,
                            description = article.description,
                            url = article.url,
                            urlImage = article.urlToImage,
                            publishedAt = article.publishedAt.toString()
                        )

                        navController.navigateTo(route = route)
                    }
                )
            }

            screen(Screen.NewsDetailsScreen) { navBackStackEntry ->
                NewsDetailsScreen(
                    author = navBackStackEntry.arguments?.getString("author").orEmpty(),
                    title = navBackStackEntry.arguments?.getString("title").orEmpty(),
                    description = navBackStackEntry.arguments?.getString("description").orEmpty(),
                    url = navBackStackEntry.arguments?.getString("url").orEmpty(),
                    urlToImage = navBackStackEntry.arguments?.getString("urlToImage").orEmpty(),
                    publishedAt = navBackStackEntry.arguments?.getString("publishedAt").orEmpty()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    NewsAppTheme {
        NewsMainListScreen(onArticleClicked = {})
    }
}
