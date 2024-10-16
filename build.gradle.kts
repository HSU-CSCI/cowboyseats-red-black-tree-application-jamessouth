plugins {
    id("java")
    application // Adding the application plugin to support running Java applications

}

group = "edu.hsutx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // You can set a default main class here if needed, or leave it unset since we define specific tasks below.
    mainClass.set("edu.hsutx.MainApp")
}

