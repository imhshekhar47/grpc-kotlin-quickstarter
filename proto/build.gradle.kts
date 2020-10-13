plugins {
    `java-library`
}

group = "${rootProject.group}"
version = "${rootProject.version}"

java {
    sourceSets.getByName("main").resources.srcDir("src/main/proto")
}