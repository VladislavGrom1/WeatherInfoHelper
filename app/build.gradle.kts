plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
}

val mapkitApiKey: String by rootProject.extra

android {
    namespace = "com.vladislavgrom.weatherinfohelper"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.vladislavgrom.weatherinfohelper"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MAPKIT_API_KEY", "\"${mapkitApiKey}\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {
    // Core
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.material.icons.core)
    implementation(libs.androidx.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    // DI
    implementation (libs.dagger.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)

    // Location
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    // Geocoding
    implementation(libs.compass.geocoder)
    implementation(libs.compass.geocoder.mobile)

    // Geolocation
    implementation(libs.compass.geolocation)
    implementation(libs.compass.geolocation.mobile)

    // YandexMapKit
    implementation(libs.maps.mobile)

    // Util
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

configurations.all {
    resolutionStrategy {
        force("io.ktor:ktor-client-core:2.3.12")
        force("io.ktor:ktor-client-okhttp:2.3.12")
        force("io.ktor:ktor-client-content-negotiation:2.3.12")
        force("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    }
}