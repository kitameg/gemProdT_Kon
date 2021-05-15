// project version
version = "3.6.0"

repositories {
    mavenCentral()
}

plugins {
    id("org.asciidoctor.jvm.convert") version "3.+"
}

tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
    baseDirFollowsSourceDir()
    setOutputDir("${buildDir}/asciidoc/${project.version}")
}

tasks.named("build") { dependsOn("asciidoctor") }
