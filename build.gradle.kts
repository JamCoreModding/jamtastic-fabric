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
    val mavenUrls = mapOf(
        Pair("https://maven.terraformersmc.com/releases", listOf("com.terraformersmc")),
        Pair("https://maven.shedaniel.me/", listOf("me.shedaniel.cloth")),
        Pair("https://api.modrinth.com/maven", listOf("maven.modrinth")),
        Pair("https://maven.blamejared.com", listOf("vazkii.patchouli")),
        Pair("https://storage.googleapis.com/devan-maven/", listOf("io.github.foa", "io.github.astrarre")),
    )

    mavenLocal()

    for (mavenPair in mavenUrls) {
        maven {
            url = uri(mavenPair.key)
            content {
                for (group in mavenPair.value) {
                    includeGroup(group)
                }
            }
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.18.2+build.local:v2"))
    })

    modImplementation(libs.loader)
    modImplementation(libs.fabric.api)

    // Required:
    modApi(libs.cloth.config)
    modApi(libs.meal.api)
    include(modApi(libs.stack.aware))

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
