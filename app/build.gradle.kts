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
}

application {
    // Define the main class for the application.
    mainClass = "metrics-board.app.App"
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}