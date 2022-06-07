@file:Suppress("SpellCheckingInspection")

plugins {
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.7.2"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.0"
    id("io.github.p03w.machete") version "1.0.11"
    id("org.cadixdev.licenser") version "0.6.1"
}

apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/publishing.gradle.kts")
apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/misc.gradle.kts")

val mod_version: String by project

group = "io.github.jamalam360"
version = mod_version

sourceSets {
    val main = this.getByName("main")

    create("gametest") {
        this.compileClasspath += main.compileClasspath
        this.compileClasspath += main.output
        this.runtimeClasspath += main.runtimeClasspath
        this.runtimeClasspath += main.output
    }
}

loom {
    mixin {
        defaultRefmapName.set("jamfabric-refmap.json")
    }

    runs {
        this.create("gametest") {
            server()
            name("Game Test")
            source(sourceSets.getByName("gametest"))
            vmArg("-Dfabric-api.gametest")
            vmArg("-Dfabric-api.gametest.report-file=${project.buildDir}/junit.xml")
            runDir("build/gametest")
        }
    }
}

repositories {
    val mavenUrls = mapOf(
        Pair("https://maven.terraformersmc.com/releases", listOf("com.terraformersmc")),
        Pair("https://maven.shedaniel.me/", listOf("me.shedaniel.cloth")),
        Pair("https://api.modrinth.com/maven", listOf("maven.modrinth")),
        Pair("https://maven.blamejared.com", listOf("vazkii.patchouli")),
        Pair("https://storage.googleapis.com/devan-maven/", listOf("io.github.foa", "io.github.astrarre")),
        Pair("https://maven.gegy.dev", listOf("dev.lambdaurora")),
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
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:${libs.versions.minecraft.get()}+build.${libs.versions.mappings.build.get()}:v2"))
    })

    // Fabric:
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    // Required:
    modApi(libs.required.cloth.config)
    modApi(libs.required.meal.api)
    //modApi(libs.required.stack.aware)

    // JiJ:
    //include(libs.required.stack.aware)

    // Dependencies of stack aware to JiJ:
    //include(libs.required.astrarre.access)
    //include(libs.required.astrarre.util)
    //include(libs.required.astrarre.itemview)

    // Optional:
    modApi(libs.optional.mod.menu)
    modApi(libs.optional.patchouli)
    modApi(libs.optional.sandwichable)

    // Runtime:
    modLocalRuntime(libs.runtime.spruce.ui) // Dependency of Sandwichable
    modLocalRuntime(libs.runtime.lazy.dfu) // For _speed_
}

tasks {
    named("test") {
        dependsOn("runGametest")
    }
}
