plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.retailHubJacoco)
    alias(libs.plugins.mokkery)
}

retailhubJacoco {
    testTask.set("testAndroidHostTest")
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.core.storage"
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
            baseName = "CoreStorageKit"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:common"))
                // koin
                implementation(libs.koin.core)
                // coroutines
                implementation(libs.coroutines.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.coroutines.test)
            }
        }
    }
}
