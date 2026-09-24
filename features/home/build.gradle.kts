plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.features.home"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        androidResources {
            enable = true
        }

        withHostTestBuilder {
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "HomeKit"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))

                // compose
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.components.resources)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
