import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    id("multiloader-common")
}

val commonJava by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val commonResources by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
dependencies {
    compileOnly(project(":common")) {
        exclude(group = "me.shedaniel.cloth", module = "*")
        exclude(group = "dev.architectury", module = "*")
    }

    "commonJava"(project(mapOf("path" to ":common", "configuration" to "commonJava")))
    "commonResources"(project(mapOf("path" to ":common", "configuration" to "commonResources")))
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(commonResources)
    from(commonResources)
}

tasks.named<Javadoc>("javadoc") {
    onlyIf { false }
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(commonJava)
    from(commonJava)
    dependsOn(commonResources)
    from(commonResources)
}
