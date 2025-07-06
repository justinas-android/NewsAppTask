package com.example.newsapp.utils.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun NavGraphBuilder.screen(
    screen: Screen,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = screen.route,
        content = content
    )
}

fun NavHostController.navigateTo(screen: Screen) {
    if (currentDestination?.route != screen.route) {
        navigate(screen.route)
    }
}
