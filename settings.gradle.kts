rootProject.name = "ms-to-mca"

pluginManagement {
    val kotlinVersion = settings.extra["kotlin.version"] as? String
        ?: throw IllegalStateException("Please specify kotlin.version in gradle.properties")

    plugins {
        kotlin("multiplatform") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion
    }
}