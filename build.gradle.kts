plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("xyz.jpenilla.run-paper") version "2.1.0"
  id("net.kyori.indra.checkstyle") version "3.1.2"
  id("com.github.ben-manes.versions") version "0.47.0"
}

group = "city.thefloating"
version = "0.1.0-SNAPSHOT"
description = "The core, monolithic plugin for The Floating City."

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  mavenCentral()
  maven("https://papermc.io/repo/repository/maven-public/")
  maven("https://repo.thbn.me/releases/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
  compileOnly("net.luckperms:api:5.4")

  implementation("broccolai.corn:corn-minecraft-paper:3.2.0")
  implementation("com.google.inject:guice:7.0.0")
  implementation("dev.tehbrian:tehlib-paper:0.4.2")
  implementation("org.spongepowered:configurate-hocon:4.1.2")
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

  jar {
    archiveBaseName.set("FloatyPlugin")
  }

  shadowJar {
    archiveClassifier.set("")

    val libsPackage = "${project.group}.${project.name}.libs"
    relocate("broccolai.corn", "$libsPackage.corn")
    relocate("cloud.commandframework", "$libsPackage.cloud")
    relocate("com.google.inject", "$libsPackage.guice")
    relocate("dev.tehbrian.tehlib", "$libsPackage.tehlib")
    relocate("org.spongepowered.configurate", "$libsPackage.configurate")
  }

  runServer {
    minecraftVersion("1.20.2")
  }
}
