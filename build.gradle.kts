import org.asciidoctor.gradle.jvm.AsciidoctorTask

version = "3.6.0"

repositories {
    mavenCentral()
}

plugins {
    id("org.asciidoctor.jvm.convert") version "3.+"
}

tasks {
  "asciidoctor"(AsciidoctorTask::class) {
    baseDirFollowsSourceDir()
    setOutputDir("${buildDir}/asciidoc/${project.version}")
  }
}