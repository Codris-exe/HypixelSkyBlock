import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation(project(":service.generic"))
    implementation(project(":type.generic"))
    implementation(project(":commons"))
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.sparkjava:spark-core:2.9.4")
}

application {
    mainClass.set("net.swofty.service.api.APIService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceAPI")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}