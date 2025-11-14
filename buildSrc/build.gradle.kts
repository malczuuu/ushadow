plugins {
    `kotlin-dsl`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.3.0.202506031305-r")
}
