import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("ChameleonApplicationKt")
}

group = "com.github.crazytosser46"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-netty:1.5.2")
    implementation("io.ktor:ktor-server-core:1.5.2")
    implementation("io.ktor:ktor-jackson:1.5.2")

    // DB
    implementation("org.jetbrains.exposed:exposed-core:0.29.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.29.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.29.1")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("com.viartemev:ktor-flyway-feature:1.2.2")
    implementation("org.flywaydb:flyway-core:7.7.3")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.postgresql:postgresql:42.2.19")

    // Log
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Json Spec
    implementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

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
