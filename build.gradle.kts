// Top-level build file (build.gradle)

plugins {
    id("com.google.devtools.ksp") version "2.0.10-1.0.24" apply false

}

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath (libs.gradle)
        classpath (libs.kotlin.gradle.plugin)
        classpath (libs.hilt.android.gradle.plugin)

        // NOTE: Do not place your application dependencies here; they belong in the individual module build.gradle files
    }

}

