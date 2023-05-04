plugins {
    id("java")
}

group = "server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
    implementation("org.reflections:reflections:0.9.12")
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.classgraph:classgraph:4.8.106")
    implementation("org.jsoup:jsoup:1.14.3")
}

tasks.test {
    useJUnitPlatform()
}