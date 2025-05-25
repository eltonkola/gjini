import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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




tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll("-Xjsr305=strict", "-java-parameters")
    }
}



// --- Distribution Tasks ---
val distLayoutDirProvider = layout.buildDirectory.dir("app-dist")
val libDistDirProvider = distLayoutDirProvider.map { it.dir("lib") }
val appJarName = "app.jar"

// Standard JAR task (thin JAR of your project's code)

tasks.jar {
    enabled = true
    archiveBaseName.set(project.name) // Or just 'app' if you prefer app.jar directly from this task
    manifest {
        attributes(
            "Main-Class" to "com.google.adk.web.AdkWebServer",
            "Class-Path" to sourceSets.main.get().runtimeClasspath.files
                .filter { it.isFile && it.name.endsWith(".jar") }
                .joinToString(" ") { file -> "lib/${file.name}" } // Path relative to app.jar
        )
    }
}


// Task to copy libraries
tasks.register<Copy>("copyLibsToDist") {
    group = "distribution"
    description = "Copies runtime dependencies to the distribution's lib folder."
    from(sourceSets.main.get().runtimeClasspath.filter { it.name.endsWith(".jar") })
    into(libDistDirProvider)

    // Ensure destination directory exists
    doFirst {
        libDistDirProvider.get().asFile.mkdirs()
    }
    // Force it to run if createCustomDistribution runs, by making it depend on a "prepare" task
}

// Task to copy the application JAR
tasks.register<Copy>("copyAppJarToDist") {
    group = "distribution"
    description = "Copies the application JAR to the distribution folder."
    dependsOn(tasks.jar)
    from(tasks.jar.flatMap { it.archiveFile }) {
        rename { _ -> appJarName }
    }
    into(distLayoutDirProvider)

    // Ensure destination directory exists
    doFirst {
        distLayoutDirProvider.get().asFile.mkdirs()
    }
}

// Task to clean and prepare the distribution directory
tasks.register("prepareDistDir") {
    group = "distribution"
    doLast { // Use doLast to ensure it runs at execution time
        val distDirFile = distLayoutDirProvider.get().asFile
        if (distDirFile.exists()) {
            project.delete(distDirFile)
            println("Cleaned distribution directory: ${distDirFile.absolutePath}")
        }
        distDirFile.mkdirs()
        libDistDirProvider.get().asFile.mkdirs() // also make the lib dir
        println("Prepared distribution directory: ${distDirFile.absolutePath}")
    }
}

// Umbrella task to create the full distribution
tasks.register("createCustomDistribution") {
    group = "distribution"
    description = "Creates the application distribution with app JAR and libs."

    // Make copy tasks depend on preparation, and this task depend on copy tasks
    dependsOn(tasks.named("prepareDistDir"))
    tasks.named("copyAppJarToDist").get().mustRunAfter(tasks.named("prepareDistDir"))
    tasks.named("copyLibsToDist").get().mustRunAfter(tasks.named("prepareDistDir"))

    dependsOn(tasks.named("copyAppJarToDist"), tasks.named("copyLibsToDist"))

    doLast {
        println("Custom distribution successfully created at: ${distLayoutDirProvider.get().asFile.absolutePath}")
    }
}

// Make the 'clean' task also clean our custom distribution directory
tasks.clean {
    delete(distLayoutDirProvider)
}
// --- End of Distribution Tasks ---

// ... (your runAdkServer task and kotlin compiler options) ...

