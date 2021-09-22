plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "xyz.tehbrian"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://s01.oss.sonatype.org/content/groups/public/") {
        name = "sonatype-s01"
    }
    maven("https://repo.broccol.ai/snapshots/") {
        name = "broccolai-snapshots"
    }

    maven("https://repo.aikar.co/content/groups/aikar/") {
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

    compileOnly("net.luckperms:api:5.0")
    implementation("com.google.inject:guice:5.0.1")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("org.spongepowered:configurate-hocon:4.0.0")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.7.2")

    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")

    implementation("broccolai.corn:corn-minecraft-paper:3.0.0-SNAPSHOT")
    implementation("dev.tehbrian:tehlib-paper:0.1.0-SNAPSHOT")
}

tasks {
    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveBaseName.set("TFCPlugin")

        relocate("co.aikar.commands", "xyz.tehbrian.tfcplugin.libs.acf")
        relocate("co.aikar.locales", "xyz.tehbrian.tfcplugin.libs.locales")
        relocate("com.github.stefvanschie.inventoryframework", "xyz.tehbrian.tfcplugin.libs.inventoryframework")
    }
}
