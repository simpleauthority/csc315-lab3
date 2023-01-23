plugins {
    id("java")
}

group = "dev.jacobandersen.calpoly.csc315"
version = "1.0.0-SNAPSHOT"

tasks.jar {
    manifest.attributes["Main-Class"] = "dev.jacobandersen.calpoly.csc315.lab2.Bootstrap"
}