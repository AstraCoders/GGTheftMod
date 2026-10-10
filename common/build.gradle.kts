plugins {
    id("multiloader-common")
    id("net.neoforged.moddev")
}

val commonJava by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

sourceSets.main.get().resources.srcDir("src/generated/resources")

artifacts {
    add(commonJava.name, sourceSets.main.get().java.sourceDirectories.singleFile)
    sourceSets.main.get().resources.sourceDirectories.files.forEach {
        add(commonResources.name, it)
    }
}

val neoforge_version: String by project

neoForge {
    version = neoforge_version
}
