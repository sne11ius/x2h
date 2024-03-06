plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.peterabeles.gversion") version "1.10.2"
}

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

gversion {
    srcDir = "src/main/kotlin/"
}

tasks.compileKotlin {
    dependsOn.add(tasks.createVersionFile)
}
