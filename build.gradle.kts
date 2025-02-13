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

    implementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}