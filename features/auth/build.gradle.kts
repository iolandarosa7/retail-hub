plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.mokkery)
    jacoco
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

val jacocoExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",

    "**/generated/resources/**",
    "**/features/auth/di/**"
)

tasks.register<JacocoReport>("jacocoCoverage") {

    group = "verification"
    description = "Generates JaCoCo coverage report for Android device tests."

    dependsOn("connectedAndroidDeviceTest")

    sourceDirectories.setFrom(
        files(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        )
    )

    classDirectories.setFrom(
        fileTree(
            layout.buildDirectory.dir("classes/kotlin/android/main")
        ) {
            include("**/*.class")
            exclude(jacocoExclusions)
        }
    )

    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include(
                "outputs/code_coverage/androidDeviceTest/**/*.ec"
            )
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.features.auth"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

        withHostTestBuilder {  }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            enableCoverage = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AuthKit"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:ui"))
            implementation(project(":core:network"))
            implementation(project(":core:model"))
            implementation(project(":core:common"))
            // ktor
            implementation(libs.ktor.client.core)
            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewModel)
            // compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            // serialization
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.compose.ui.test)
            implementation(libs.ktor.client.serialization)
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