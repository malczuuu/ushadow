import com.diffplug.spotless.LineEnding
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("java")
    id("org.springframework.boot").version("3.0.0")
    id("io.spring.dependency-management").version("1.1.7")
    id("com.diffplug.spotless").version("8.0.0")
}

group = "io.github.malczuuu"
version = "1.0.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        content {
            includeGroup("io.github.malczuuu.problem4j")
        }
        mavenContent {
            snapshotsOnly()
        }
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    implementation("io.github.malczuuu.problem4j:problem4j-spring-webmvc:1.0.0-SNAPSHOT") {
        isChanging = true
    }
}

spotless {
    format("misc") {
        target("**/*.gradle.kts", "**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    java {
        target("src/**/*.java")

        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        lineEndings = LineEnding.UNIX
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    systemProperty("user.language", "en")
    systemProperty("user.country", "US")

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.SHORT
        showStandardStreams = true
    }
}
