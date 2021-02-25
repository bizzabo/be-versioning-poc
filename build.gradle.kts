import org.asciidoctor.gradle.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  val kotlinVersion = "1.4.0"

  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion
  id("org.springframework.boot") version "2.4.0"
  id("io.spring.dependency-management") version "1.0.10.RELEASE"
  id("org.asciidoctor.convert") version "1.5.3"
}

group = "com.bizzabo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/libs-milestone") }

  maven {
    url = uri("s3://repository.bizzabo.com/maven2/repos/snapshots")

    mavenContent {
      snapshotsOnly()
    }

    content {
      includeGroup("com.bizzabo")
    }

    authentication {
      create<AwsImAuthentication>("awsIm")
    }
  }

  maven {
    url = uri("s3://repository.bizzabo.com/maven2/repos/releases")

    mavenContent {
      releasesOnly()
    }

    content {
      includeGroup("com.bizzabo")
    }

    authentication {
      create<AwsImAuthentication>("awsIm")
    }
  }
}

dependencies {
  val springRestdocs = "2.0.4.RELEASE"
  val springAutoRestdocs = "2.0.8"

  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-actuator")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  implementation("com.bizzabo:spring-mdc:1.3.2-SNAPSHOT")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("io.projectreactor:reactor-test")

  // test docs
  asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:$springRestdocs")
  testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient:$springRestdocs")
  testImplementation("capital.scalable:spring-auto-restdocs-core:$springAutoRestdocs")
}

val snippetsDir = file("build/generated-snippets")

tasks.withType<Test> {
  useJUnitPlatform()
  outputs.dir(snippetsDir)
}

tasks.withType<AsciidoctorTask> {
  inputs.dir(snippetsDir)
  dependsOn("test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}

tasks.withType<BootJar> {
  dependsOn("asciidoctor")
  mainClassName = "com.bizzabo.spring.example.SpringExampleApplicationKt"
  archiveFileName.set("springexample.jar")
  launchScript()
}

tasks.withType<Wrapper> {
  version = "6.5"
}
