plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
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
    android {
        namespace = "com.iolandarosa.retailhub.core.model"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {  }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CoreModelKit"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.client.serialization)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}