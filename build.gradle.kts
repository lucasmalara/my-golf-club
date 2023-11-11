plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.mygolfclub"
version = "1.0.0"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.5")
	implementation("org.springframework.boot:spring-boot-starter-security:3.1.5")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.1.5")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.2.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:3.1.5")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	compileOnly("org.projectlombok:lombok:1.18.30")
	runtimeOnly("com.mysql:mysql-connector-j:8.0.33")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
