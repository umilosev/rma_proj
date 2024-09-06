import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // Updated to match your Kotlin version
    id("com.google.devtools.ksp") version "2.0.10-1.0.24" // Use the compatible version with Kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.10" // Added serialization plugin
}

android {
    namespace = "com.example.rma_proj_esus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rma_proj_esus"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3" // Ensure compatibility with Compose
    }

    packaging {
        resources {
            excludes.addAll(listOf("/META-INF/{AL2.0,LGPL2.1}"))
        }
    }
}

dependencies {

    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.6.0")
    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // https://mvnrepository.com/artifact/com.jakewharton.retrofit/retrofit2-kotlinx-serialization-converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-kotlinx-serialization
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")


    /// https://mvnrepository.com/artifact/androidx.room/room-ktx
    implementation(libs.androidx.room.ktx)

    // https://mvnrepository.com/artifact/org.danekja/jdk-serializable-functional
    implementation("org.danekja:jdk-serializable-functional:1.8.1")

    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // OkHttp logging interceptor
    implementation(libs.logging.interceptor.v500alpha14)

    // Core Libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

    // Compose Libraries
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")

    //Activity
    implementation("androidx.activity:activity-ktx:1.7.2")

    // Jetpack Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Room for Database
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    ksp("com.google.dagger:hilt-compiler:2.51")

    // Coroutines and Flow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")

    // Coil for Image Loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Navigation Fragment KTX
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation(libs.androidx.navigation.ui.ktx)
}
