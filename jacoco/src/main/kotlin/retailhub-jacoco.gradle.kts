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

    reportsDirectory.set(
        layout.buildDirectory.dir("reports/jacoco")
    )
}

tasks.register<JacocoReport>("jacocoCoverage") {

    group = "verification"
    description = "Generates JaCoCo coverage report for this KMP Android module."

    sourceDirectories.setFrom(
        files("src/commonMain/kotlin", "src/androidMain/kotlin")
    )

    classDirectories.setFrom(
        jacocoExtension.exclusions.map { exclusions ->
            fileTree(layout.buildDirectory.dir("classes/kotlin/android/main")) {
                include("**/*.class")
                exclude(exclusions)
            }
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

    dependsOn(jacocoExtension.testTask)
}
