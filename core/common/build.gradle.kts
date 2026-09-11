import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.retailHubJacoco)
}

retailhubJacoco {
    testTask.set("testAndroidHostTest")
}

buildkonfig {
    packageName = "com.iolandarosa.retailhub.core.common"

    val secretPropertiesFile = rootProject.file("secrets.properties")
    val secretProperties = Properties()
    secretProperties.load(FileInputStream(secretPropertiesFile))

    val mapboxToken: String = secretProperties.getProperty("MAPBOX_ACCESS_TOKEN") ?: ""

    defaultConfigs {
        buildConfigField(STRING, "MAPBOX_TOKEN", mapboxToken)
    }
}

kotlin {
    android {
        namespace = "com.iolandarosa.retailhub.core.common"
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
            baseName = "CoreCommonKit"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            // koin
            implementation(libs.koin.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
