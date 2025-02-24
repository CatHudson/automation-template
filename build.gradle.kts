plugins {
    kotlin("jvm") version "2.1.0"
}

group = "homework"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.selenide)
    implementation(libs.restAssured)
    implementation(libs.allure)
    implementation(libs.jackson)
    implementation(libs.coroutines)
    implementation(libs.wiremock)

    testImplementation(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
    testImplementation(libs.junitParams)
    testImplementation(libs.assertJ)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
