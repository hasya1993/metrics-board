plugins {
    id("buildlogic.java-application-conventions")
    idea
    java
    id("org.springframework.boot") version "3.2.4"
}

dependencies {
    implementation(project(":persistence"))
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
}

application {
    // Define the main class for the application.
    mainClass = "metrics-board.app.App"
}

tasks.named <Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}