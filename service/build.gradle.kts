plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "com.genes"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":persistence"))
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    implementation("org.json:json:20240303")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.1")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.3")
    testImplementation("org.mockito:mockito-core:3.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
}
