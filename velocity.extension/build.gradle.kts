import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.0.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.viaversion.com")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    api(files("velocity-proxy-3.4.0-SNAPSHOT.jar"))

    implementation("com.github.Swofty-Developments:AtlasRedisAPI:1.1.3")
    // implementation("net.swofty:AtlasRedisAPI:1.1.4")
    implementation(project(":commons"))

    implementation("com.viaversion:vialoader:4.0.2")
    implementation("com.viaversion:viabackwards-common:5.3.2")
    implementation("com.viaversion:viarewind-common:4.0.7")
    implementation("com.viaversion:viaversion:5.3.2")

    implementation("io.netty:netty-all:4.1.110.Final")

    implementation("net.bytebuddy:byte-buddy:1.14.16")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.16")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
}

evaluationDependsOn(":commons")

application {
    mainClass.set("net.swofty.velocity.SkyBlockVelocity")
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "net.swofty.velocity.SkyBlockVelocity"
    }
    archiveBaseName.set("SkyBlockProxy")
    archiveClassifier.set("")
    archiveVersion.set("")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["shadowJar"])
        }
    }
}