plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

group = "homework"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    detektPlugins(libs.detektFormatting)
    implementation(libs.selenide)
    implementation(libs.restAssured)
    implementation(libs.allure)
    implementation(libs.allureRestAssured)
    implementation(libs.allureSelenide)
    implementation(libs.jackson)
    implementation(libs.coroutines)
    implementation(libs.wiremock)
    implementation(libs.swaggerCoverage)

    testImplementation(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
    testImplementation(libs.junitParams)
    testImplementation(libs.assertJ)
}

tasks {
    withType(Test::class) {
        testLogging {
            events("failed")
        }
    }
    test {
        useJUnitPlatform {
            excludeTags("Setup")
        }
    }
    register<Test>("server-setup") {
        useJUnitPlatform {
            includeTags("Server-setup")
        }
    }
    register<Test>("agent-setup") {
        useJUnitPlatform {
            includeTags("Agent-setup")
        }
    }
    register<Test>("test-regression") {
        useJUnitPlatform {
            includeTags("Regression")
        }
    }
}
kotlin {
    jvmToolchain(21)
}

ktlint {
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    }
}
