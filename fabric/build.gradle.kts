plugins {
    id("idea")
    id("multiloader-loader")
    id("net.fabricmc.fabric-loom-remap")
    id("net.darkhax.curseforgegradle")
    id("com.modrinth.minotaur")
}

val minecraft_version: String by project
val java_version: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project
val mod_id: String by project
val mod_version: String by project
val curseforge_project_id: String by project
val modrinth_project_id: String by project

base.archivesName.set("${mod_id}-fabric-${minecraft_version}")

repositories {
    maven("https://cursemaven.com") {
        content { includeGroup("curse.maven") }
    }
}

loom {
    mixin {
        defaultRefmapName = "theft.refmap.json"
    }
    mods {
        create(mod_id) { sourceSet(sourceSets.main.get()) }
    }

    runs {
        named("client") {
            client()
            displayName.set("Fabric Client")
            generateRunConfig.set(true)
        }

        create("client2") {
            client()
            displayName.set("Fabric Client 2")
            generateRunConfig.set(true)
            programArguments.addAll(listOf("--username", "Dev2"))
        }

        named("server") {
            server()
            displayName.set("Fabric Server")
            generateRunConfig.set(true)
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")
    modImplementation("curse.maven:forge-config-api-port-547434:7213608")
    compileOnly("com.electronwill.night-config:toml:3.8.0")
}

tasks.processResources {
    val properties = mapOf(
        "version" to mod_version,
        "minecraft_version" to minecraft_version,
        "java_version" to java_version,
        "fabric_loader_version" to fabric_loader_version,
        "fabric_api_version" to fabric_api_version
    )
    inputs.properties(properties)
    filesMatching("fabric.mod.json") { expand(properties) }
}

tasks.register<net.darkhax.curseforgegradle.TaskPublishCurseForge>("publishCurseForge") {
    dependsOn(tasks.named("remapJar"))
    group = "publishing"
    apiToken = rootProject.extra["curseforgeKey"] as String
    val mainFile = upload(curseforge_project_id.toLong(), tasks.named("remapJar"))
    mainFile.releaseType = "release"
    mainFile.changelogType = "markdown"
    mainFile.changelog = rootProject.extra["modChangelog"] as String
    mainFile.addGameVersion(minecraft_version)
    mainFile.addModLoader("Fabric")
}

modrinth {
    token.set(rootProject.extra["modrinthKey"] as String)
    projectId.set(modrinth_project_id)
    versionNumber.set("$mod_version-$minecraft_version-fabric")
    versionName.set("$mod_version for Fabric $minecraft_version")
    versionType.set("release")
    uploadFile.set(tasks.named("remapJar"))
    gameVersions.add(minecraft_version)
    changelog.set(rootProject.extra["modChangelog"] as String)
    loaders.add("fabric")
}
