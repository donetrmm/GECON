package com.gecon

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gecon.core.navigation.AppNavHost
import com.gecon.core.ui.components.GestoControlBottomBar
import com.gecon.core.ui.theme.GestoControlTheme
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GestoControlApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            val faceDetectorOptions = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build()

            FaceDetection.getClient(faceDetectorOptions)

            Log.d("GestoControlApp", "ML Kit inicializado correctamente")
        } catch (e: Exception) {
            Log.e("GestoControlApp", "Error al inicializar ML Kit", e)
        }
    }
}

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