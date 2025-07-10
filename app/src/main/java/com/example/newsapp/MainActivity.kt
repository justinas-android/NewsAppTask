package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.designsystem.NewsAppTheme
import com.example.newsapp.ui.news.details.NewsDetailsScreen
import com.example.newsapp.ui.news.list.NewsMainListScreen
import com.example.newsapp.ui.news.models.Article
import com.example.newsapp.ui.webview.WebViewScreen
import com.example.newsapp.utils.analytics.AnalyticsTracker
import com.example.newsapp.utils.navigation.Screen
import com.example.newsapp.utils.navigation.navigateTo
import com.example.newsapp.utils.navigation.screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                MainFlow(
                    analyticsTracker = analyticsTracker,
                    onExit = { this@MainActivity.finish() }
                )
            }
        }
    }
}

@Composable
fun MainFlow(
    navController: NavHostController = rememberNavController(),
    analyticsTracker: AnalyticsTracker,
    onExit: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        if (!navController.popBackStack()) {
                            onExit()
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
                        val route = Screen.NewsDetailsScreen.createRoute(article)
                        navController.navigateTo(route = route)
                    }
                )
            }

            screen(Screen.NewsDetailsScreen) { navBackStackEntry ->
                val json = navBackStackEntry.arguments?.getString("articleJson") ?: ""
                val article = Json.decodeFromString<Article>(json)

                NewsDetailsScreen(
                    article = article,
                    onReadFullArticleClicked = { url ->
                        val route = Screen.WebViewScreen.createRoute(url)
                        navController.navigateTo(route = route)
                    }
                )
            }

            screen(Screen.WebViewScreen) { navBackStackEntry ->
                WebViewScreen(url = navBackStackEntry.arguments?.getString("url").orEmpty())
            }
        }
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val screenName = when (destination.route) {
                Screen.NewsMainListScreen.route -> "NewsMainListScreen"
                Screen.NewsDetailsScreen.route -> "NewsDetailsScreen"
                Screen.WebViewScreen.route -> "WebViewScreen"
                else -> "UnknownScreen"
            }
            analyticsTracker.logScreen(screenName)
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
