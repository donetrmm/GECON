package com.gecon

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gecon.core.navigation.AppNavHost
import com.gecon.core.ui.components.GestoControlBottomBar
import com.gecon.core.ui.theme.GestoControlTheme
import dagger.hilt.android.HiltAndroidApp
import android.app.Application

@HiltAndroidApp
class GestoControlApplication : Application()

@Composable
fun GestoControlApp() {
    GestoControlTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                GestoControlBottomBar(navController = navController)
            }
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}