plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(26)
}

javafx {
    version = "26.0.1"
    modules("javafx.controls")
}

application {
    mainClass.set("CustomerManager.CustomerManagerApp")
    applicationDefaultJvmArgs = listOf("--enable-native-access=javafx.graphics")
}
