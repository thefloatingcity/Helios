plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.kyori.indra.checkstyle") version "2.1.1"
}

group = "xyz.tehbrian"
version = "0.1.0-SNAPSHOT"
description = "The core, monolithic plugin for The Floating City."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://repo.broccol.ai/snapshots/") {
        name = "broccolai-snapshots"
    }
    maven("https://repo.thbn.me/releases/") {
        name = "thbn"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")

    implementation("com.google.inject:guice:5.1.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")

    implementation("broccolai.corn:corn-minecraft-paper:3.0.0-SNAPSHOT")
    implementation("dev.tehbrian:tehlib-paper:0.3.1")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version, "description" to project.description)
        }
    }

    shadowJar {
        archiveBaseName.set("FloatyPlugin")
        archiveClassifier.set("")

        val libsPackage = "xyz.tehbrian.floatyplugin.libs"
        relocate("com.google.inject", "$libsPackage.guice")
        relocate("cloud.commandframework", "$libsPackage.cloud")
        relocate("org.spongepowered.configurate", "$libsPackage.configurate")
        relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
        relocate("broccolai.corn", "$libsPackage.corn")
    }

    runServer {
        minecraftVersion("1.19")
    }
}
