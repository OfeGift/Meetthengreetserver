val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val mongodbKotlin: String by project
val koinVersion: String by project
val commonsCodecVersion: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

//    implementation("org.mongodb:mongodb-driver-kotlin:$mongodbKotlin")
    implementation("org.mongodb:bson-kotlinx:$mongodbKotlin")
    implementation("org.mongodb:bson-kotlin:$mongodbKotlin")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:$mongodbKotlin")

    implementation("commons-codec:commons-codec:$commonsCodecVersion")

    //Koin core features
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    sshAntTask("org.apache.ant:ant-jsch:1.10.12")
}

/**For Docker and any Plugin packaging*/
tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.wsj.ApplicationKt"))
        }
    }
}

ktor {
    fatJar {
        archiveFileName.set("com.wsj.meetthengreet-server-$version-all.jar")
    }
}
