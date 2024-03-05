import com.palantir.gradle.gitversion.VersionDetails
import java.util.*

plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.palantir.git-version") version "3.0.0"
}
val versionDetails: groovy.lang.Closure<VersionDetails> by extra
val details = versionDetails()
version = details.gitHash

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

application.applicationName = "x2h"

tasks.withType<Jar> {
    archiveFileName.set("x2h.jar")
    manifest {
        attributes["Main-Class"] = "X2hKt"
    }
    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

dependencies {
    val jacksonVersion = "2.15.3"
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("com.github.ProjectMapK:jackson-module-kogera:2.16.1-beta11")
    implementation("com.typesafe:config:1.4.3")
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<ProcessResources>() {
    doLast {
        val propertiesFile = file("${layout.buildDirectory.get()}/resources/main/version.properties")
        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", rootProject.version.toString())
        propertiesFile.writer().use { properties.store(it, null) }
    }
}
