plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("xyz.jpenilla.run-paper") version "2.2.0"
  id("net.kyori.indra.checkstyle") version "3.1.3"
  id("com.github.ben-manes.versions") version "0.48.0"
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
  implementation("dev.tehbrian:tehlib-paper:0.5.0")
  implementation("cloud.commandframework:cloud-paper:1.8.3")
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
    fun moveToLibs(vararg patterns: String) {
      for (pattern in patterns) {
        relocate(pattern, "$libsPackage.$pattern")
      }
    }

    moveToLibs(
      "broccolai.corn",
      "cloud.commandframework",
      "com.typesafe",
      "com.google",
      "dev.tehbrian.tehlib",
      "io.leangen",
      "jakarta.inject",
      "javax.annotation",
      "org.aopalliance",
      "org.checkerframework",
      "org.spongepowered",
    )
  }

  runServer {
    minecraftVersion("1.20.2")
  }
}
