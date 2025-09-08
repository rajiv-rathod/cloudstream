buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath(libs.gradle)
        classpath(libs.jetbrains.kotlin.gradle.plugin)
        classpath(libs.dokka.gradle.plugin)
        // Universal build config
        classpath(libs.buildkonfig.gradle.plugin)
        // Compose Multiplatform
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.7.3")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jogamp.org/deployment/maven")
    }
}