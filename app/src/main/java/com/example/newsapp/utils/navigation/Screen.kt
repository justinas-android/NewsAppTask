package com.example.newsapp.utils.navigation

import android.net.Uri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.newsapp.ui.news.models.Article
import kotlinx.serialization.json.Json

sealed class Screen(
    val baseRoute: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    val route: String
        get() = buildRoute()

    open fun buildRoute(): String = baseRoute

    object WebViewScreen : Screen(
        baseRoute = "webViewScreen",
        arguments = listOf(
            navArgument("url") { type = NavType.StringType },
        )
    ) {
        fun createRoute(url: String): String {
            val encodedUrl = Uri.encode(url)
            return "$baseRoute/$encodedUrl"
        }

        override fun buildRoute(): String = "$baseRoute/{url}"
    }

    object NewsMainListScreen : Screen("newsMainListScreen")

    object NewsDetailsScreen : Screen(
        baseRoute = "newsDetailsScreen",
        arguments = listOf(
            navArgument("articleJson") { type = NavType.StringType }
        )
    ) {
        fun createRoute(article: Article): String {
            val json = Uri.encode(Json.encodeToString(article))
            return "$baseRoute/$json"
        }

        override fun buildRoute(): String = "$baseRoute/{articleJson}"
    }
}
