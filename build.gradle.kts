import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

val protobufVersion = "3.12.0"
val grpcVersion = "1.32.1"
val grpcKotlinVersion = "0.1.5"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.0"
    id("com.google.protobuf") version "0.8.13"
}

group = "org.hshekhar.grpc"
version = "1.0.0"

sourceSets.named("main") {
    withConvention(KotlinSourceSet::class) {
        kotlin.srcDirs(
                "src/main/java",
                "src/main/kotlin",
                "${buildDir}/generated/source/proto/main/java",
                "${buildDir}/generated/source/proto/main/grpc",
                "${buildDir}/generated/source/proto/main/grpckt"
        )
    }
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")


    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("io.grpc:grpc-services:$grpcVersion")

    runtimeOnly("io.grpc:grpc-netty:$grpcVersion")

    testImplementation("io.grpc:grpc-testing:$grpcVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }

    dependsOn("generateProto")
}

