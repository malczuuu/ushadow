import com.diffplug.spotless.LineEnding

plugins {
    id("java")
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot)
}

group = "io.github.malczuuu.ushadow"

/**
 * In order to avoid hardcoding snapshot versions, we derive the version from the current Git commit hash. For CI/CD add
 * -Pversion={releaseVersion} parameter to match Git tag.
 */
version =
    if (version == "unspecified") {
        getSnapshotVersion(rootProject.rootDir)
    } else {
        version
    }

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    implementation(platform(libs.problem4j.spring.bom))
    implementation(platform(libs.springdoc.openapi.bom))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.data.mongodb)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    implementation(libs.problem4j.spring.webmvc)

    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    runtimeOnly(libs.micrometer.registry.prometheus)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.rabbit.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.mongodb)
    testImplementation(libs.testcontainers.rabbitmq)
    testRuntimeOnly(libs.junit.platform.launcher)
}

spotless {
    format("misc") {
        target("**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    java {
        target("**/src/**/*.java")

        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        lineEndings = LineEnding.UNIX
    }

    kotlin {
        target("**/src/**/*.kt")

        ktfmt("0.59").metaStyle()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlinGradle {
        target("**/*.gradle.kts")

        ktlint("1.7.1").editorConfigOverride(mapOf("max_line_length" to "120"))
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("user.language", "en")
    systemProperty("user.country", "US")

    testLogging {
        events("passed", "skipped", "failed")
    }
}
