import org.gradle.api.attributes.Attribute

plugins {
    `java-library`
}

val java_version: String by project

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(java_version.toInt()))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.named(variant) {
        attributes.attribute(loaderAttribute, "common")
    }
}

sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes.attribute(loaderAttribute, "common")
        }
    }
}
