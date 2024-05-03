plugins {
	id("buildlogic.java-application-conventions")
	idea
	java
	id("org.springframework.boot") version "3.2.4"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")
	compileOnly("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")
	runtimeOnly("org.postgresql:postgresql:42.7.3")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:10.11.0")
	testImplementation("org.testcontainers:postgresql:1.19.7")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
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