import com.android.build.gradle.tasks.PackageAndroidArtifact
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "cn.lalaki.sample.dialog"
    compileSdkPreview = "CinnamonBun"
    defaultConfig {
        applicationId = namespace
        minSdk = 21
        //noinspection EditedTargetSdkVersion
        targetSdk = 36
        versionCode = 1
        versionName =
            "$versionCode.${
                ZonedDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("MMdd"))
            }"
    }
    signingConfigs {
        register("lalaki_config") {
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
            keyAlias = "dazen@189.cn"
            storeFile = file("D:\\imoe.jks")
            storePassword = System.getenv("mystorepass")
            keyPassword = System.getenv("mystorepass2")
        }
    }
    buildTypes {
        named("release") {
            signingConfig = signingConfigs.getByName("lalaki_config")
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            isJniDebuggable = false
            isDebuggable = false
            setProguardFiles(
                setOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                ),
            )
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_20
        }
    }
    packaging.resources.excludes.addAll(
        mutableSetOf(
            "META-INF",
            "META-INF/**",
            "kotlin/**",
            "DebugProbesKt.bin",
            "kotlin-tooling-metadata.json",
        ),
    )
    buildToolsVersion = "37.0.0 rc2"
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation(project(":library"))
}

tasks.withType<PackageAndroidArtifact> {
    doFirst { appMetadata.asFile.get().writeText("") }
}
