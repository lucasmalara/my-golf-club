plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
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

val versionSpringBootStarter: String = "3.2.1"
val versionThymeleafSecExtras: String = "3.1.2.RELEASE"
val versionOpenAPI: String = "2.3.0"
val versionLombok: String = "1.18.30"
val versionMySQLCon: String = "8.2.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$versionSpringBootStarter")
	implementation("org.springframework.boot:spring-boot-starter-security:$versionSpringBootStarter")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:$versionSpringBootStarter")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:$versionThymeleafSecExtras")
	implementation("org.springframework.boot:spring-boot-starter-validation:$versionSpringBootStarter")
	implementation("org.springframework.boot:spring-boot-starter-web:$versionSpringBootStarter")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:$versionSpringBootStarter")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$versionOpenAPI")
	compileOnly("org.projectlombok:lombok:$versionLombok")
	runtimeOnly("com.mysql:mysql-connector-j:$versionMySQLCon")
	annotationProcessor("org.projectlombok:lombok:$versionLombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test:$versionSpringBootStarter")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
