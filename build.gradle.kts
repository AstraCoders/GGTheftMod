plugins {
    id("net.neoforged.moddev") version "2.0.141" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.18-SNAPSHOT" apply false
    id("net.darkhax.curseforgegradle") version "1.3.33" apply false
    id("com.modrinth.minotaur") version "2.8.7" apply false
}

group = property("mod_group_id") as String
version = property("mod_version") as String

val localSecrets = java.util.Properties().apply {
    rootProject.file("secrets.properties").takeIf { it.isFile }?.inputStream()?.use { load(it) }
}
extra["curseforgeKey"] = providers.environmentVariable("CURSEFORGE_KEY_SECRET")
    .orElse(localSecrets.getProperty("curseforge_key") ?: "DUMMY").get()
extra["modrinthKey"] = providers.environmentVariable("MODRINTH_KEY_SECRET")
    .orElse(localSecrets.getProperty("modrinth_key") ?: "DUMMY").get()

extra["modChangelog"] = runCatching {
    val commit = providers.environmentVariable("GIT_COMMIT").orNull
        ?: providers.exec { commandLine("git", "log", "-n", "1", "--pretty=tformat:%h") }
            .standardOutput.asText.get().trim()
    val previousCommit = providers.environmentVariable("GIT_PREVIOUS_COMMIT").orNull
    val args = if (previousCommit.isNullOrBlank()) {
        listOf("git", "log", "--pretty=tformat:- %s", "-1", commit)
    } else {
        listOf("git", "log", "--pretty=tformat:- %s", "$previousCommit..$commit")
    }
    providers.exec { commandLine(args) }.standardOutput.asText.get().trim()
}.getOrElse {
    logger.warn("Changelog could not be generated: ${it.message}")
    ""
}.ifBlank { rootProject.file("CHANGELOG.md").readText() }
