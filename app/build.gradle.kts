plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.gecon"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gecon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // MediaPipe para detección de manos y caras (similar a la CNN)
    implementation(libs.tasks.vision)

// ML Kit para detección como backup
    implementation(libs.face.detection)
    implementation(libs.object1.detection.v1700)
    // Core Android
    implementation(libs.androidx.core.ktx.v1150)
    implementation(libs.androidx.lifecycle.runtime.ktx.v287)
    implementation(libs.androidx.activity.compose.v1101)

    implementation(platform(libs.androidx.compose.bom.v20250301))
    //noinspection UseTomlInstead
    implementation(libs.androidx.compose.ui.ui2)
    implementation(libs.androidx.compose.ui.ui.graphics2)
    implementation(libs.androidx.compose.ui.ui.tooling.preview2)
    implementation(libs.androidx.compose.material3.material32)
    implementation(libs.androidx.compose.material.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.vision.common)
    implementation(libs.play.services.mlkit.face.detection)
    implementation(libs.object1.detection.common)
    implementation(libs.object1.detection)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // TensorFlow Lite
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)

    // Coil para carga de imágenes
    implementation(libs.coil.compose)

    // Accompanist para permisos
    implementation(libs.accompanist.permissions)

    // ViewModel para Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose.v287)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20250301))
    androidTestImplementation(libs.androidx.compose.ui.ui.test.junit42)

    // Debug
    debugImplementation(libs.androidx.compose.ui.ui.tooling2)
    debugImplementation(libs.androidx.compose.ui.ui.test.manifest2)
}

kapt {
    correctErrorTypes = true
}