object JacocoConfig {
    const val VERSION = "0.8.14"

    val defaultExclusions =
        listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*\$serializer.class",
        )
}
