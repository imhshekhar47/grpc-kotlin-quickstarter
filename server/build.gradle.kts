plugins {
    `application`
    kotlin("jvm")
    id("com.google.protobuf")
}

group = "${rootProject.group}"
version = "${rootProject.version}"

dependencies {
    implementation(project(":stub"))

    implementation("io.grpc:grpc-services:${rootProject.extra["grpcVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutineVersion"]}")

    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")

    testImplementation("io.grpc:grpc-testing:${rootProject.ext["grpcVersion"]}")
}

application {
    mainClassName = "orh.hshekhar.grpc.AppKt"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}
