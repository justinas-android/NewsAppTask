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
        arguments = screen.arguments,
        content = content
    )
}

fun NavHostController.navigateTo(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}
