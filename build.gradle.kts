plugins {
    id("java")
}

group = "server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.1")
    implementation("junit:junit:4.13.1")
    implementation("junit:junit:4.13.1")
    implementation("junit:junit:4.13.1")
    implementation("org.testng:testng:7.1.0")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
    implementation("org.reflections:reflections:0.9.12")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.classgraph:classgraph:4.8.106")
}

tasks.test {
    useJUnitPlatform()
}