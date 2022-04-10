plugins {
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.6.0"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.0.0"
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
        "https://maven.blamejared.com",
        "https://cursemaven.com",
        "https://storage.googleapis.com/devan-maven/"
    )

    for (url in mavenUrls) {
        maven(url = url)
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.18.2+build.21:v2"))
    })

    modImplementation(libs.loader)
    modImplementation(libs.fabric.api)

    // Required:
    modApi(libs.cloth.config)
    modApi(libs.meal.api)
    modApi(libs.stack.aware)

    // Optional:
    modApi(libs.mod.menu)
    modApi(libs.patchouli)
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
        archiveBaseName.set("Jamtastic")
    }

    remapJar {
        archiveBaseName.set("Jamtastic")
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}