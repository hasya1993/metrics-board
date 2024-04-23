plugins {
    id("buildlogic.java-application-conventions")
    idea
    java
    id("org.springframework.boot") version "3.2.4"
}

dependencies {
    implementation(project(":persistence"))
    implementation("org.apache.commons:commons-text")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    implementation("org.springframework.data:spring-data-relational:3.2.4")
    implementation("org.flywaydb:flyway-core:10.11.0")
    implementation("org.postgresql:postgresql:42.7.3")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.11.0")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.springframework:spring-test:6.1.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
}

application {
    // Define the main class for the application.
    mainClass = "metrics-board.app.App"
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}