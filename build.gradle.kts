import java.util.Properties

plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.hibernate.orm") version "7.4.1.Final"
	id("org.graalvm.buildtools.native") version "1.1.1"
	id("org.springframework.cloud.contract") version "5.0.3"
	id("org.asciidoctor.jvm.convert") version "4.0.5"
}

group = "com.moiras"
version = property("version")

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["springCloudVersion"] = "2025.1.2"
extra["springModulithVersion"] = "2.1.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.springframework.modulith:spring-modulith-observability-api")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	implementation("org.springframework.modulith:spring-modulith-starter-jpa")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
	runtimeOnly("org.springframework.modulith:spring-modulith-observability-core")
	runtimeOnly("org.springframework.modulith:spring-modulith-runtime")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
	testImplementation("org.springframework.boot:spring-boot-starter-hateoas-test")
	testImplementation("org.springframework.boot:spring-boot-starter-restdocs")
	testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

hibernate {
	enhancement {
	}
}

contracts {
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.contractTest {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)
}

tasks.named("bootRun") {
	outputs.upToDateWhen { false }
}

tasks.register("version") {

    group = "versioning"
    description = "Incrementa la versión."

    doLast {

        val type = findProperty("type")?.toString()
            ?: throw GradleException(
                "Uso: ./gradlew version -Ptype=patch|minor|major"
            )

        val file = file("gradle.properties")

        val properties = Properties()

        file.inputStream().use {
            properties.load(it)
        }

        val current = properties.getProperty("version")

        val snapshot = current.endsWith("-SNAPSHOT")

        val clean = current.removeSuffix("-SNAPSHOT")

        val numbers = clean
            .split(".")
            .map { it.toInt() }
            .toMutableList()

        when (type) {

            "patch" -> {
                numbers[2]++
            }

            "minor" -> {
                numbers[1]++
                numbers[2] = 0
            }

            "major" -> {
                numbers[0]++
                numbers[1] = 0
                numbers[2] = 0
            }

            else ->
                throw GradleException("Tipo inválido: $type")
        }

        val next =
            numbers.joinToString(".") +
                    if (snapshot) "-SNAPSHOT" else ""

        properties["version"] = next

        file.outputStream().use {
            properties.store(it, null)
        }

        println()
        println("Versión: $current → $next")
        println()
    }
}
