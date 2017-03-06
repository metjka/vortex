import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.tasks.wrapper.Wrapper

val kotlin_version = "1.1.0"

buildscript {

    repositories {
        gradleScriptKotlin()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }

}

plugins {
    application
}

application {
    mainClassName = "io.metjka.vortex.ui.VortApplication"
}

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8

}
apply {
    plugin("kotlin")
}

tasks{
    "goodbye"{
        dependsOn("build")
        doLast { println("Bye!") }
    }
}

task<Wrapper>("wrapper"){
    gradleVersion ="3.5-20170227190532+0000"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        setUrl("http://dl.bintray.com/kotlin/kotlin-eap-1.1/")
    }

    dependencies {
        compileClasspath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        compileClasspath("de.dynamicfiles.projects.gradle.plugins", "javafx-gradle-plugin", "8.8.2")
    }
}

dependencies {
    compile("io.reactivex", "rxkotlin", "0.60.0")
    compile("org.slf4j", "slf4j-simple", "1.7.22")
    compile("com.google.code.gson", "gson", "2.8.0")
    compile("com.google.guava", "guava", "20.0")
    compile("org.jetbrains.kotlin", "kotlin-reflect", kotlin_version)
    compile("org.jetbrains.kotlin", "kotlin-stdlib", kotlin_version)
    compile("io.github.microutils", "kotlin-logging", "1.4.1")

    testCompile("junit", "junit", "4.12")
}
