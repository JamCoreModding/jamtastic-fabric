plugins {
    id("fabric-loom") version "0.10-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower-mini") version "1.2.1"
    id("org.quiltmc.quilt-mappings-on-loom") version "3.1.1"
    id("org.cadixdev.licenser") version "0.6.1"
}

val modVersion: String by project

group = "io.github.jamalam360"
version = modVersion

loom {
    mixin {
        defaultRefmapName.set("jamfabric-refmap.json")
    }
}

repositories {
    val mavenUrls = listOf(
        "https://maven.terraformersmc.com/releases",
        "https://maven.shedaniel.me/",
        "https://api.modrinth.com/maven",
        "https://maven.blamejared.com"
    )

    for (url in mavenUrls) {
        maven(url = url)
    }
}

dependencies {
    val minecraftVersion: String by project
    val mappingsVersion: String by project
    val loaderVersion: String by project
    val fabricApiVersion: String by project
    val clothConfigVersion: String by project
    val modMenuVersion: String by project
    val mealApiVersion: String by project
    val patchouliVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:$minecraftVersion+build.$mappingsVersion:v2"))
    })

    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    // Required:
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion")
    modApi("maven.modrinth:mealapi:$mealApiVersion")

    // Optional:
    modApi("com.terraformersmc:modmenu:$modMenuVersion")
    modApi("vazkii.patchouli:Patchouli:$patchouliVersion")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }

    build {
        dependsOn("updateLicenses")
    }

    jar {
        archiveBaseName.set("Jam")
    }

    remapJar {
        archiveBaseName.set("Jam")
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}