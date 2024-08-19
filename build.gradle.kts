plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
    kotlin("jvm") version PluginVersions.JVM_VERSION
    kotlin("plugin.serialization") version "1.9.22"
    kotlin("kapt") version PluginVersions.KAPT_VERSION
}

val snippetsDir by extra { file("build/generated-snippets")}

group = "xquare.app"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

ext {
    set("springBootVersion", PluginVersions.SPRING_BOOT_VERSION)
    set("grpcVersion", "1.62.2")
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

    // vault
    implementation("com.bettercloud:vault-java-driver:5.1.0")

    // kubernetes
    implementation("io.kubernetes:client-java:15.0.1")
    implementation("software.amazon.awssdk:sts:2.17.64")

    implementation("org.springframework.boot:spring-boot-starter-websocket:2.7.18")

    // OpenTelemetry && gRPC
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${project.ext["springBootVersion"]}"))
    implementation("io.opentelemetry.proto:opentelemetry-proto:1.3.2-alpha")
    implementation("com.google.protobuf:protobuf-java-util:3.25.3")
    implementation("io.grpc:grpc-netty-shaded:${project.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${project.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-stub:${project.ext["grpcVersion"]}")
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")
    implementation("io.micrometer:micrometer-core")

    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
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
