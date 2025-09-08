import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.7.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10"
}

val javaTarget = JvmTarget.fromTarget("1.8")

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    // Compose Desktop
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    
    // Core library
    implementation(project(":library"))
    
    // Networking (already compatible)
    implementation("com.github.Blatzar:NiceHttp:0.4.13")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    
    // Image loading for desktop
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    
    // File operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")
    
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

compose.desktop {
    application {
        mainClass = "com.lagradost.cloudstream3.desktop.MainKt"
        
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "CloudStream"
            packageVersion = "4.5.5"
            description = "CloudStream Desktop - Stream movies and TV shows"
            copyright = "© 2024 CloudStream Team. Licensed under GPL v3."
            vendor = "CloudStream Team"
            licenseFile.set(project.file("../LICENSE"))
            
            windows {
                console = false
                dirChooser = true
                perUserInstall = true
                upgradeUuid = "18159995-d967-4cd2-8885-77f3374e1234" // Fixed UUID for upgrades
                
                // App metadata
                packageName = "CloudStream Desktop"
                packageVersion = "4.5.5"
                
                // Exe specific settings
                shortcut = true
                menu = true
                
                // Code signing configuration
                // Uncomment and configure when you have a certificate
                // signing {
                //     sign.set(true)
                //     identity.set(System.getenv("SIGNING_IDENTITY") ?: "CloudStream Team")
                //     keystore.set(file(System.getenv("KEYSTORE_PATH") ?: "certificate.p12"))
                //     password.set(System.getenv("KEYSTORE_PASSWORD") ?: "")
                //     timestamp.set("http://timestamp.sectigo.com")
                // }
            }
            
            linux {
                packageName = "cloudstream-desktop"
                debMaintainer = "cloudstream@example.com"
                menuGroup = "AudioVideo"
                appCategory = "AudioVideo"
            }
            
            macOS {
                bundleID = "com.lagradost.cloudstream3.desktop"
                packageName = "CloudStream Desktop"
                dockName = "CloudStream"
                appCategory = "public.app-category.entertainment"
            }
        }
        
        // JVM arguments for better performance
        jvmArgs += listOf(
            "-Xmx2G",
            "-Djava.awt.headless=false",
            "-Dfile.encoding=UTF-8"
        )
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(javaTarget)
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}

// Task to create a simple JAR for distribution
tasks.register<Jar>("fatJar") {
    group = "distribution"
    description = "Create a fat JAR with all dependencies"
    archiveClassifier.set("all")
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "com.lagradost.cloudstream3.desktop.MainKt"
    }
}