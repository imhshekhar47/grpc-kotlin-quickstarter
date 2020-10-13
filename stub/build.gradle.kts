import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    `java-library`
    kotlin("jvm")
    id("com.google.protobuf")
}

group = "${rootProject.group}"
version = "${rootProject.version}"

sourceSets.named("main") {
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs(
                "src/main/java",
                "src/main/kotlin",
                "${buildDir}/generated/source/proto/main/java",
                "${buildDir}/generated/source/proto/main/grpc",
                "${buildDir}/generated/source/proto/main/grpckt"
        )
    }
}

dependencies {
    protobuf(project(":proto"))
    api("com.google.protobuf:protobuf-java-util:${rootProject.extra["protobufVersion"]}")
    api("io.grpc:grpc-protobuf:${rootProject.extra["grpcVersion"]}")
    api("io.grpc:grpc-stub:${rootProject.extra["grpcVersion"]}")
    api("io.grpc:grpc-kotlin-stub:${rootProject.extra["grpcKotlinVersion"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.extra["protobufVersion"]}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.extra["grpcVersion"]}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.extra["grpcKotlinVersion"]}"
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("generateProto")
}

