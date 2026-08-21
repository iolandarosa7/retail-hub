plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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

    "**/theme/**",
    "**/generated/resources/**"
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
        namespace = "com.iolandarosa.retailhub.core.ui"
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
            baseName = "CoreUiKit"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:model"))

            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
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