@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") version "1.6.0"
}

group = "me.nullicorn.ooze"
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

    sourceSets {
        val commonMain by getting {}

        val jvmMain by getting {
            dependencies {
                // Used internally for JSON serializing & deserializing.
                implementation("com.google.code.gson:gson:2.8.9")
            }
        }
    }
}
