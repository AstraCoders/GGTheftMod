plugins {
    id("idea")
    id("multiloader-loader")
    id("net.neoforged.moddev")
    id("net.darkhax.curseforgegradle")
    id("com.modrinth.minotaur")
}

val minecraft_version: String by project
val neoforge_version: String by project
val mod_id: String by project
val mod_version: String by project
val curseforge_project_id: String by project
val modrinth_project_id: String by project

base.archivesName.set("${mod_id}-neoforge-${minecraft_version}")

sourceSets.main.get().resources.srcDir("src/generated/resources")

repositories {
    maven("https://maven.theillusivec4.top/")
    maven {
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
}

neoForge {
    version = neoforge_version

    runs {
        configureEach {
            gameDirectory.set(project.file("runs/${name}"))
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }
        create("client") { client() }
        create("client1") {
            client()
            programArguments.addAll("--username", "Dev1")
        }
        create("server") { server() }
        create("data") {
            data()
            programArguments.addAll("--mod", mod_id, "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }
    }

    mods {
        create(mod_id) { sourceSet(sourceSets.main.get()) }
    }
}

tasks.processResources {
    val properties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "neoforge_version_range" to project.property("neoforge_version_range"),
        "neoforge_loader_version_range" to project.property("neoforge_loader_version_range"),
        "mod_id" to mod_id,
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )
    inputs.properties(properties)
    filesMatching("META-INF/neoforge.mods.toml") { expand(properties) }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("top.theillusivec4.curios:curios-neoforge:9.5.1+1.21.1:api")
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:9.5.1+1.21.1")

    runtimeOnly("curse.maven:shatterbyte-lib-916747:8040848")
    runtimeOnly("curse.maven:relics-mod-445274:8158315")
    runtimeOnly("curse.maven:architectury-api-419699:8492726")
}

tasks.register<net.darkhax.curseforgegradle.TaskPublishCurseForge>("publishCurseForge") {
    dependsOn(tasks.named("jar"))
    group = "publishing"
    apiToken = rootProject.extra["curseforgeKey"] as String
    val mainFile = upload(curseforge_project_id.toLong(), tasks.named<Jar>("jar"))
    mainFile.releaseType = "release"
    mainFile.changelogType = "markdown"
    mainFile.changelog = rootProject.extra["modChangelog"] as String
    mainFile.addGameVersion(minecraft_version)
    mainFile.addModLoader("NeoForge")
}

modrinth {
    token.set(rootProject.extra["modrinthKey"] as String)
    projectId.set(modrinth_project_id)
    versionNumber.set("$mod_version-$minecraft_version-neoforge")
    versionName.set("$mod_version for NeoForge $minecraft_version")
    versionType.set("release")
    uploadFile.set(tasks.named<Jar>("jar"))
    gameVersions.add(minecraft_version)
    changelog.set(rootProject.extra["modChangelog"] as String)
    loaders.add("neoforge")
}
