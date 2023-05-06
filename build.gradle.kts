plugins {
    id("java")
	id("jacoco")
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
    implementation("io.github.classgraph:classgraph:4.8.106")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation ("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation ("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation ("org.postgresql:postgresql:42.2.27")
}

tasks.test {
    useJUnitPlatform()
}
