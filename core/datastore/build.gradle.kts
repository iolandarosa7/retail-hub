plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.retailHubJacoco)
}

retailhubJacoco {
    testTask.set("testAndroidHostTest")

    exclusions.add("**/core/datastore/factory/**")
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.core.datastore"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        withHostTestBuilder {
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CoreDatastoreKit"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.datastore.core)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.koin.core)
                implementation(project(":core:model"))
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
