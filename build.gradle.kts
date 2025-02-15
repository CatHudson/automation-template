plugins {
    kotlin("jvm") version "2.1.0"
}

group = "homework"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val junitVersion = "5.11.4"
    val seleniumVersion = "4.28.1"
    val restAssuredVersion = "5.5.0"
    val allureVersion = "2.29.1"
    val jacksonVersion = "2.18.2"

    implementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")
    implementation("io.qameta.allure:allure-junit5:$allureVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
