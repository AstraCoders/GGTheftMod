pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
    }
}



plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "theft"
include("common", "fabric", "neoforge")
