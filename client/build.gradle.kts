plugins {
    `java-library`
    kotlin("jvm")
}

group = "${rootProject.group}"
version = "${rootProject.version}"

dependencies {
    implementation(project(":stub"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutineVersion"]}")

    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutineVersion"]}")
    testImplementation("org.mockito:mockito-core:3.5.13")
}

