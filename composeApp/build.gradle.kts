import org.jetbrains.kotlin.gradle.dsl.JvmTarget // Add this import

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.8.0" // Ensure the Kotlin Serialization plugin is applied
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11) // Set JVM target to version 11
        }
    }

    // iOS Target Configuration
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    val ktor_version = "2.3.0"

    sourceSets {
        // Android Main Source Set
        androidMain.dependencies {
            implementation(compose.preview) // Compose preview for Android
            implementation(libs.androidx.activity.compose)
        }

        // Common Main Source Set
        commonMain.dependencies {
            // Compose dependencies
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // AndroidX Lifecycle dependencies for Compose
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Ktor dependencies for networking
            implementation("io.ktor:ktor-client-core:$ktor_version")
            implementation("io.ktor:ktor-client-cio:$ktor_version") // Ktor CIO engine for client
            implementation("io.ktor:ktor-client-content-negotiation:$ktor_version") // Content Negotiation
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version") // JSON serialization plugin

            // Kotlinx Serialization dependencies
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Latest version

            // Kotlin Logging for debugging
            implementation("io.github.microutils:kotlin-logging:2.1.23")
        }

        // Android Source Set for platform-specific code
        androidMain.dependencies {
            // Android specific dependencies
            implementation("io.ktor:ktor-client-android:$ktor_version")
        }

        // iOS Source Set for platform-specific code
        iosMain.dependencies {
            // iOS specific dependencies for Ktor client
            implementation("io.ktor:ktor-client-ios:$ktor_version")
        }
    }
}

android {
    namespace = "org.maizer.news"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.maizer.news"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Exclude unnecessary resources
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true // Enable minification for release builds
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling) // Add compose UI tooling for debugging
}
