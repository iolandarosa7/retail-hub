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

subprojects {
    plugins.withId("retailhub-jacoco") {
        val jacocoCoverage = tasks.named<JacocoReport>("jacocoCoverage")
        aggregate.configure {
            dependsOn(jacocoCoverage)
            sourceDirectories.from(jacocoCoverage.map { it.sourceDirectories })
            classDirectories.from(jacocoCoverage.map { it.classDirectories })
            executionData.from(jacocoCoverage.map { it.executionData })
        }
    }
}
