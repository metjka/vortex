import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    application
}

application {
    mainClassName = "samples.HelloWorld"
}

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

repositories {
    jcenter()
    mavenCentral()
    maven { "http://dl.bintray.com/kotlin/kotlin-eap-1.1/"  }
}

dependencies {
    //    compile()

    testCompile("junit:junit:4.12")
}