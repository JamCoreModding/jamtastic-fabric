plugins {
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.6.0"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.0.0"
    id("io.github.p03w.machete") version "1.0.10"
    id("org.cadixdev.licenser") version "0.6.1"
}

apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/publishing.gradle.kts")
apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/misc.gradle.kts")

val mod_version: String by project

group = "io.github.jamalam360"
version = mod_version

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
        Pair("https://maven.gegy.dev", listOf("dev.lambdaurora"))
    )

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
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.18.2+build.22:v2"))
    })

    // Fabric:
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    // Required:
    modApi(libs.required.cloth.config)
    modApi(libs.required.meal.api)
    //modApi(libs.required.stack.aware)

    // JiJ:
    //include(libs.required.aware)

    // Optional:
    modApi(libs.optional.mod.menu)
    modApi(libs.optional.patchouli)
    modApi(libs.optional.sandwichable)

    // Runtime:
    modLocalRuntime(libs.runtime.spruce.ui) // Dependency of Sandwichable
}

tasks {
    named("prepareRemapJar") {
        dependsOn("optimizeOutputsOfJar")
    }

    named("remapJar") {
        dependsOn("optimizeOutputsOfJar")
    }
}
