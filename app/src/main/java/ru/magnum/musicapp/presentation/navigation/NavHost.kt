package ru.magnum.musicapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.magnum.musicapp.presentation.сatalog.CatalogScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogScreen
    ) {
        composable<CatalogScreen> {
            CatalogScreen()
        }
    }
}