plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
    kotlin("jvm") version PluginVersions.JVM_VERSION
    kotlin("plugin.serialization") version "1.9.22"
}

group = "xquare.app"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.KOTLIN_REFLECT)
    implementation(Dependencies.KOTLIN_JDK)
    implementation(Dependencies.JACKSON)

    implementation(Dependencies.HEALTH_CHECKER)

    implementation(Dependencies.SPRING_CLOUD)
    implementation(Dependencies.SPRING_WEB)
    implementation(Dependencies.OPEN_FEIGN)
    implementation(Dependencies.FEIGN_HTTP)
    implementation(Dependencies.SPRING_SECURITY)
    implementation(Dependencies.JWT)
    implementation(Dependencies.REDIS)
    implementation(Dependencies.LOGGER)
    implementation(Dependencies.SPRING_VALIDATION)

    // web
    implementation(Dependencies.SPRING_WEB)

    // validation
    implementation(Dependencies.SPRING_VALIDATION)

    implementation(Dependencies.SPRING_TRANSACTION)
    implementation(Dependencies.JWT)

    // MySQL
    implementation(Dependencies.MYSQL_CONNECTOR)

    // jpa
    implementation(Dependencies.SPRING_DATA_JPA)

    // redis
    implementation(Dependencies.REDIS)
    implementation(Dependencies.ANNOTATION_PROCESSOR)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${DependencyVersions.SPRING_CLOUD_VERSION}")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.jar {
    enabled = false
}
