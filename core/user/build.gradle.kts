plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.retailHubJacoco)
}

retailhubJacoco {
    testTask.set("testAndroidHostTest")

    exclusions.add("**/core/user/di/**")
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.core.user"
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
            baseName = "CoreUserKit"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:model"))
            implementation(project(":core:network"))
            implementation(project(":core:storage"))
            implementation(project(":core:datastore"))
            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.serialization)
            // koin
            implementation(libs.koin.core)
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.coroutines.test)
                implementation(libs.ktor.client.mock)
            }
        }
    }
}
