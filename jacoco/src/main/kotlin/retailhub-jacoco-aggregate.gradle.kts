import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    jacoco
}

jacoco {
    toolVersion = JacocoConfig.VERSION
}

val aggregate =
    tasks.register<JacocoReport>("jacocoCoverageAggregate") {

        group = "verification"

        description =
            "Generates the unified JaCoCo coverage report for all KMP modules."

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(false)

            html.outputLocation.set(
                layout.buildDirectory.dir(
                    "reports/jacoco/aggregate/html"
                )
            )

            xml.outputLocation.set(
                layout.buildDirectory.file(
                    "reports/jacoco/aggregate/jacoco.xml"
                )
            )
        }
    }

gradle.projectsEvaluated {

    rootProject.subprojects.forEach { project ->

        val jacocoExtension =
            project.extensions.findByName("retailhubJacoco")
                    as? RetailhubJacocoExtension
                ?: return@forEach

        val exclusions =
            jacocoExtension.exclusions.get()

        aggregate.configure {

            dependsOn(
                project.tasks.named("jacocoCoverage")
            )

            sourceDirectories.from(
                files(
                    project.file("src/commonMain/kotlin"),
                    project.file("src/androidMain/kotlin")
                )
            )

            classDirectories.from(
                project.fileTree(
                    project.layout.buildDirectory.dir(
                        "classes/kotlin/android/main"
                    )
                ) {
                    include("**/*.class")
                    exclude(exclusions)
                }
            )

            executionData.from(
                project.fileTree(
                    project.layout.buildDirectory
                ) {
                    include("jacoco/**/*.exec")
                    include("outputs/code_coverage/**/*.ec")
                }
            )
        }
    }
}