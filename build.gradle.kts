@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")

    // Documentation generator.
    id("org.jetbrains.dokka")

    // Publishing to Sonatype & Maven Central.
    id("java")
    id("signing")
    id("maven-publish")
}
apply(from = "gradle/publish.gradle.kts")

group = "me.nullicorn"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    js(BOTH) {
        nodejs()
        browser()
    }

    sourceSets {
        val commonMain by getting {}
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                // For JSON serializing & deserializing.
                implementation("com.github.cliftonlabs:json-simple:4.0.0")
            }
        }

        val jsMain by getting {
            dependencies {
                // For sending requests to Microsoft/Minecraft services.
                implementation(npm("sync-fetch", "^0.3.1"))
            }
        }
    }
}

tasks.dokkaHtml.configure {
    dokkaSourceSets {
        val commonMain by getting {
            val dokkaBase = File(projectDir, "src/commonMain/resources")

            // Code snippets available to the @sample tag in KDoc.
            val sampleFiles = File(dokkaBase, "samples")
                .walk()
                .maxDepth(1)
                .filter { it.extension == "kt" }

            // Package-level documentation.
            val packageDocFiles = File(dokkaBase, "package_docs")
                .walk()
                .maxDepth(1)
                .filter { it.extension == "md" }

            samples.from(*sampleFiles.toList().toTypedArray())
            includes.from(*packageDocFiles.toList().toTypedArray())
        }
    }
}