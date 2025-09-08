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
            description = "CloudStream Desktop - Movie and TV Show Streaming"
            copyright = "© 2024 CloudStream. Licensed under GPL v3."
            vendor = "CloudStream Team"
            
            windows {
                console = false
                dirChooser = true
                perUserInstall = true
                
                // Code signing configuration - will be configured later
                // signing {
                //     sign.set(true)
                //     identity.set("CloudStream Team")
                // }
            }
        }
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