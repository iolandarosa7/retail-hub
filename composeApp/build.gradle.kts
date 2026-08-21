import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    "**/*\$serializer.class",
)

tasks.register<JacocoReport>("jacocoCoverage") {
    group = "verification"
    description = "Generates JaCoCo coverage report for this module."

    dependsOn("testAndroidHostTest")

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
            include("jacoco/testAndroidHostTest.exec")
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AppKit"
            isStatic = true
        }
    }
    
    android {
       namespace = "com.iolandarosa.retailhub.composeapp"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_21
       }

        withHostTestBuilder {  }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.koin.core)

            implementation(project(":core:common"))
            implementation(project(":core:network"))
            implementation(project(":core:ui"))
            implementation(project(":features:auth"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}