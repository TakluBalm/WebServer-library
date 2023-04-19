plugins {
    id("java")
}

group = "server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.postgresql:postgresql:42.6.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}