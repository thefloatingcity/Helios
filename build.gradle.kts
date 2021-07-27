plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "xyz.tehbrian"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

repositories {
    mavenLocal()

    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "sonatype-s01"
        url = uri("https://s01.oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    compileOnly("net.luckperms:api:5.0")
    implementation("com.google.inject:guice:5.0.1")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("org.spongepowered:configurate-hocon:4.0.0")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.7.2")

    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")
}

tasks.processResources {
    filesMatching("**/plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    archiveBaseName.set("TFCPlugin")

    relocate("co.aikar.commands", "xyz.tehbrian.tfcplugin.libs.acf")
    relocate("co.aikar.locales", "xyz.tehbrian.tfcplugin.libs.locales")
    relocate("com.github.stefvanschie.inventoryframework", "xyz.tehbrian.tfcplugin.libs.inventoryframework")
}
