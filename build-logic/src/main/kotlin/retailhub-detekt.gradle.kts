import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    id("io.gitlab.arturbosch.detekt")
}

configure<DetektExtension> {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("${project.rootDir}/config/detekt/detekt.yml"))
    source.setFrom(files("src"))
}

// We don't add detekt-formatting here if we use Spotless,
// to avoid conflicts. Or we can keep it and configure it.
// For now, let's keep it but I will disable the rules in detekt.yml
dependencies {
    "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}
