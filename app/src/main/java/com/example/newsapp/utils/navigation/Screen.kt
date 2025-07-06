package com.example.newsapp.utils.navigation

import android.net.Uri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
            navArgument("author") { type = NavType.StringType },
            navArgument("title") { type = NavType.StringType },
            navArgument("description") { type = NavType.StringType },
            navArgument("url") { type = NavType.StringType },
            navArgument("urlToImage") { type = NavType.StringType },
            navArgument("publishedAt") { type = NavType.StringType },
        )
    ) {
        fun createRoute(
            author: String,
            title: String,
            description: String,
            url: String,
            urlToImage: String,
            publishedAt: String
        ): String {
            val encodedAuthor = Uri.encode(author)
            val encodedTitle = Uri.encode(title)
            val encodedDescription = Uri.encode(description)
            val encodedUrl = Uri.encode(url)
            val encodedUrlImage = Uri.encode(urlToImage)
            val encodedPublishedAt = Uri.encode(publishedAt)

            return "$baseRoute/$encodedAuthor/$encodedTitle/$encodedDescription/$encodedUrl/$encodedUrlImage/$encodedPublishedAt"
        }

        override fun buildRoute(): String = "$baseRoute/{author}/{title}/{description}/{url}/{urlToImage}/{publishedAt}"
    }
}
