import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.retailHubJacoco)
}

retailhubJacoco {
    testTask.set("connectedAndroidDeviceTest")

    exclusions.add("**/generated/resources/**")
    exclusions.add("**/composeApp/di/**")
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeAppKit"
            isStatic = true
        }
    }

    android {
        namespace = "com.iolandarosa.retailhub.composeapp"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }

        androidResources {
            enable = true
        }

        withHostTestBuilder { }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            enableCoverage = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.ktor.client.serialization)
            implementation(libs.compose.components.resources)

            implementation(project(":core:common"))
            implementation(project(":core:network"))
            implementation(project(":core:datastore"))
            implementation(project(":core:ui"))
            implementation(project(":features:auth"))
            implementation(project(":features:profile"))
            implementation(project(":core:model"))
            implementation(project(":core:storage"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
            implementation(libs.koin.compose.viewModel)
            // turbine
            implementation(libs.turbine)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.runner)
                implementation(libs.androidx.testExt.junit)
                implementation(libs.compose.ui.test.manifest)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
