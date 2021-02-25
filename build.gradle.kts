val protobufVersion : String by extra("3.12.0")
val grpcVersion: String by extra("1.32.1")
val grpcKotlinVersion: String by extra("0.1.5")
val coroutineVersion: String by extra("1.3.9")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.0"
    id("org.sonarqube") version "3.1.1"
    id("com.google.protobuf") version "0.8.13" apply(false)
}

group = "org.hshekhar.grpc"
version = "1.0.0"

repositories {
    mavenCentral()
    google()
    jcenter()
}

subprojects {
    apply (plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    sonarqube {
        properties {
            property("sonar.sourceEncoding", "UTF-8")
            //property("sonar.sources", "src/main")
        }
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}



