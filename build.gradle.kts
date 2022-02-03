plugins {
    java
    id("org.springframework.boot") version "2.6.3"
}
apply(plugin = "io.spring.dependency-management")

group "com.dptablo.straview"
version "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    /* Spring */
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    /* Data */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.3.2")

    /* springdoc */
    //implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.5")
    implementation("org.springdoc:springdoc-openapi-webmvc-core:1.6.5")

    /* dev */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    /* testing */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-core:4.3.1")
    testImplementation("com.h2database:h2:2.1.210")
}

java {
    toolchain {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}