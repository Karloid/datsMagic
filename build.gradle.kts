plugins {
    kotlin("jvm") version "2.0.20"
}

group = "com.krld"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}