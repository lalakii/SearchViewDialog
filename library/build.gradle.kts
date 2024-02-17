import com.android.build.gradle.internal.tasks.AarMetadataTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "cn.lalaki.dialog"
    compileSdk = 34
    version = 1.6
    defaultConfig {
        minSdk = 11
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions.jvmTarget = "17"
    base.archivesName = rootProject.name
}

dependencies{
    implementation("com.belerweb:pinyin4j:2.5.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0-alpha01")
}

tasks.withType<AarMetadataTask> {
    isEnabled = false
}

tasks.configureEach {
    if (name.contains("AarMetaData")) {
        enabled = false
    }
    if (name == "assembleRelease") {
        doLast {
            val desc = "A simple dialog box that supports pinyin search and multi-selection."
            val buildRoot = file("${project.layout.buildDirectory.get()}\\outputs\\aar")
            val pomXml = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- This module was also published with a richer model, Gradle metadata,  -->
  <!-- which should be used instead. Do not delete the following line which  -->
  <!-- is to indicate to Gradle or any Gradle module metadata file consumer  -->
  <!-- that they should prefer consuming it instead. -->
  <!-- do_not_remove: published-with-gradle-metadata -->
  <modelVersion>4.0.0</modelVersion>
  <groupId>cn.lalaki</groupId>
  <artifactId>${rootProject.name}</artifactId>
  <version>${version}</version>
  <packaging>aar</packaging>
  <name>${rootProject.name}</name>
  <description>${desc}</description>
  <url>https://github.com/lalakii/${rootProject.name}</url>
  <inceptionYear>2024</inceptionYear>
  <dependencies>
    <dependency>
      <groupId>com.belerweb</groupId>
      <artifactId>pinyin4j</artifactId>
      <version>2.5.1</version>
    </dependency>
    <dependency>
      <groupId>androidx.recyclerview</groupId>
      <artifactId>recyclerview</artifactId>
      <version>1.4.0-alpha01</version>
    </dependency>
  </dependencies>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <developers>
    <developer>
      <name>lalakii</name>
      <email>dazen@189.cn</email>
      <organization>lalaki.cn</organization>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:https://github.com/lalakii/${rootProject.name}.git</connection>
    <url>https://github.com/lalakii/${rootProject.name}</url>
  </scm>
</project>
"""
            val pomFile = file("${buildRoot.absolutePath}\\${rootProject.name}-${version}.pom")
            pomFile.writeText(pomXml)
            sign(pomFile.parentFile, "*.pom")
            sign(pomFile.parentFile, "*.aar")
            val osName = System.getProperty("os.name").lowercase()
            if (osName.contains("windows"))
                openExplorer()
        }
    }
}

fun openExplorer() {
    exec {
        executable = "cmd.exe"
        args(
            "/c",
            "sleep",
            "3",
            "&&",
            "start",
            "${project.layout.buildDirectory.get()}\\outputs\\aar\\"
        )
    }
}

fun sign(path: File, fileName: String) {
    try {
        exec {
            workingDir = path
            executable = "gpg"
            args("--yes", "--armor", "--detach-sign", fileName)
        }
    } catch (_: Exception) {
        System.err.println("Please install GnuPG: https://gnupg.org/download/")
    }
}