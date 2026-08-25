import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    jacoco
}

val jacocoExtension =
    extensions.create<RetailhubJacocoExtension>("retailhubJacoco")

jacocoExtension.exclusions.convention(
    JacocoConfig.defaultExclusions
)

extensions.configure<JacocoPluginExtension> {
    toolVersion = JacocoConfig.VERSION

    reportsDirectory =
        layout.buildDirectory.dir("reports/jacoco")
}

val jacocoCoverage =
    tasks.register<JacocoReport>("jacocoCoverage") {

        group = "verification"

        description =
            "Generates JaCoCo coverage report for this KMP Android module."

        sourceDirectories.setFrom(
            files(
                "src/commonMain/kotlin",
                "src/androidMain/kotlin"
            )
        )

        classDirectories.setFrom(
            fileTree(
                layout.buildDirectory.dir(
                    "classes/kotlin/android/main"
                )
            ) {
                include("**/*.class")
                exclude(jacocoExtension.exclusions.get())
            }
        )

        executionData.setFrom(
            fileTree(layout.buildDirectory) {
                include("jacoco/**/*.exec")
                include("outputs/code_coverage/**/*.ec")
            }
        )

        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
    }

afterEvaluate {
    jacocoExtension.testTask.orNull?.let { testTaskName ->
        jacocoCoverage.configure {
            dependsOn(testTaskName)
        }
    }
}