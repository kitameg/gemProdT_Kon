// project version
version = "4.x-WIP"
val git by extra("https://github.com/kitameg/${project.name}.git")

repositories {
    mavenCentral()
}

plugins {
    id("org.asciidoctor.jvm.convert") version "3.+"
}

tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
    baseDirFollowsSourceDir()
}

tasks.named("build") { dependsOn("asciidoctor") }

task<org.asciidoctor.gradle.jvm.AsciidoctorTask>("ghpagesCreateRoot") {
    setSourceDir(file("."))
    sources(delegateClosureOf<PatternSet> {
      include("README.adoc")
    })
    setOutputDir(layout.buildDirectory) 
}

task<Exec>("ghpagesClone") {
    delete("${buildDir}/ghpages")
    mkdir("${buildDir}/ghpages")
    workingDir("${buildDir}/ghpages")
    commandLine("git", "clone", git, "-b", "ghpages")
}

task<Copy>("ghpagesCopyAsciidoc") {
    dependsOn("build")
    from(layout.buildDirectory.dir("docs/asciidoc"))
    into(layout.buildDirectory.dir("ghpages/${project.name}/${project.version}"))
}

task("publish") {
    dependsOn("ghpagesCopyAsciidoc", "ghpagesClone")
    doLast {
        exec {
            workingDir("${buildDir}/ghpages/${project.name}")
            commandLine("git", "add", "*")
        } 
        exec {
            workingDir("${buildDir}/ghpages/${project.name}")
            commandLine("git", "commit", "-m", "Publish version ${project.version}", "--amend")
        } 
        exec {
            workingDir("${buildDir}/ghpages/${project.name}")
            commandLine("git", "push", "-f", "-u", "origin", "ghpages")
        } 
    }

}

task<Copy>("ghpagesCopyRoot") {
    dependsOn("ghpagesCreateRoot", "ghpagesClone")
    from(layout.buildDirectory.file("${buildDir}/README.html"))
    into(layout.buildDirectory.file("ghpages/${project.name}"))
    rename("README.html", "index.html")
}

task("publishRoot") {
    dependsOn("ghpagesCopyRoot")
    doLast {
        exec {
            workingDir("${buildDir}/ghpages/${project.name}")
            commandLine("git", "add", "index.html")
        } 
        exec {
            workingDir("${buildDir}/ghpages/${project.name}")
            commandLine("git", "commit", "-m", "Publish index.html", "--amend")
        } 
        exec {
            workingDir("${buildDir}/ghpages/${project.name}")
            commandLine("git", "push", "-f", "-u", "origin", "ghpages")
        } 
    }
}