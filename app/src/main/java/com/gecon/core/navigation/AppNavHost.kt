package com.gecon.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gecon.home.presentation.HomeScreen
import com.gecon.recognition.presentation.RecognitionScreen
import com.gecon.settings.presentation.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Recognition.route) {
            RecognitionScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}