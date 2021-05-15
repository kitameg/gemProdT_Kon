version = "3.6.0"

repositories {
    mavenCentral()
}

plugins {
    id("org.asciidoctor.jvm.convert") version "3.1.0"
}

tasks {
  "asciidoctor"(org.asciidoctor.gradle.jvm.AsciidoctorTask::class) {
    sources(delegateClosureOf<PatternSet> {
      include("index.adoc")
    })
  }
}