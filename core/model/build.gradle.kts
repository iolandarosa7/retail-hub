plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.retailHubJacoco)
}

retailhubJacoco {
    testTask.set("testAndroidHostTest")
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.core.model"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        withHostTestBuilder { }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
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
