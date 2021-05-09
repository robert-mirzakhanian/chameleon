import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.node-gradle.node") version "2.2.2"

    kotlin("jvm") version "1.4.30"
    application
}

group = "com.github.crazytosser46"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
