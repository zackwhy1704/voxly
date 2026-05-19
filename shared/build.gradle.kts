import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.animation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Coroutines
            implementation(libs.coroutines.core)

            // Serialization
            implementation(libs.serialization.json)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.serialization.json)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.tab)
            implementation(libs.voyager.transitions)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android)
            implementation(libs.coroutines.android)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.voxly.shared"
    compileSdk = 34
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("VoxlyDatabase") {
            packageName.set("com.voxly.shared.data.local")
            srcDirs("src/commonMain/sqldelight")
        }
    }
}
