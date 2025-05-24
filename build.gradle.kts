plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "1.9.21"
    application
}

group = "com.eltonkola"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.adk:google-adk:+")
    implementation("com.google.adk:google-adk-dev:+")
    implementation("io.opentelemetry:opentelemetry-sdk-trace:1.38.0")

    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.2.4")

    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7") // Or another engine like OkHttp, Apache
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")


    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.google.adk.web.AdkWebServer")
}

val googleApiKey: String? = project.findProperty("GOOGLE_API_KEY") as String?

tasks.register<JavaExec>("runAdkServer") {
    group = "application"
    description = "Runs the ADK Web Server"
    val mainRuntimeClasspath = sourceSets.main.get().runtimeClasspath
    classpath = mainRuntimeClasspath
    mainClass.set("com.google.adk.web.AdkWebServer")

    // Make sure classes are compiled first
    dependsOn("compileKotlin")

    args = listOf(
        "--adk.agents.source-dir=src/main/kotlin",
        "--adk.agents.package=com.eltonkola",
      //  "--adk.agents.class=com.eltonkola.MultiToolAgent",
        "--debug"
    )



    if (googleApiKey != null) {
        environment("GOOGLE_API_KEY", googleApiKey)
    }
    environment("GOOGLE_GENAI_USE_VERTEXAI", "FALSE")

    doFirst {
        println("Classpath for runAdkServer (ADK default dependencies):")
        mainRuntimeClasspath.files.forEach {
            println("  ${it.absolutePath}")
        }
        println("--- End of Classpath ---")
    }

    dependsOn("build")
}