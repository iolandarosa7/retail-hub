import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("com.diffplug.spotless")
}

configure<SpotlessExtension> {
    kotlin {
        licenseHeader(
            """
/*
 *
 * @Copyright ${java.time.Year.now().value} Iolanda Rosa
 *
 */
 
            """.trimIndent(),
            "^(package|import|class|object|interface|fun|typealias|val|var)\\b",
        )
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**/*.gradle.kts")
        ktlint()
    }
}
