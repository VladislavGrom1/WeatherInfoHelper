import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("com.google.gms.google-services") version "4.5.0" apply false
}

fun getMapkitApiKey(): String {
    val properties = Properties()
    val localProperties = rootProject.file("local.properties")
    if (localProperties.exists()) {
        localProperties.inputStream().use { properties.load(it) }
    }
    return properties.getProperty("MAPKIT_API_KEY", "") ?: ""
}

extra["mapkitApiKey"] = getMapkitApiKey()