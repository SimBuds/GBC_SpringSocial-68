plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "ca.georgebrown"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.7.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.testcontainers:testcontainers-bom:1.18.1")
    implementation("org.testcontainers:junit-jupiter:1.18.1")
    implementation("org.testcontainers:mockserver:1.18.1")
    implementation("org.testcontainers:postgresql:1.18.1")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave:2.16.4")
    implementation("io.micrometer:micrometer-observation:1.11.4")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.1.4")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
