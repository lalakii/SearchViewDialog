import com.android.build.gradle.tasks.PackageAndroidArtifact
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "cn.lalaki.sample.dialog"
    compileSdkPreview = "VanillaIceCream"
    defaultConfig {
        applicationId = namespace
        minSdk = 21
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
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
            isMinifyEnabled = false
            isShrinkResources = false
            isCrunchPngs = true
            isJniDebuggable = false
            isDebuggable = true
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
    kotlinOptions.jvmTarget = "20"
    packaging.resources.excludes.addAll(
        mutableSetOf(
            "META-INF",
            "META-INF/**",
            "kotlin/**",
            "DebugProbesKt.bin",
            "kotlin-tooling-metadata.json",
        ),
    )
    buildToolsVersion = "35.0.0 rc4"
}

dependencies {
    implementation(project(":library"))
    //noinspection GradleDependency
    implementation("androidx.appcompat:appcompat:1.7.0-rc01")
}

tasks.withType<PackageAndroidArtifact> {
    doFirst { appMetadata.asFile.get().writeText("") }
}

tasks.configureEach {
    if (arrayOf("aarmetadata", "artprofile", "debug", "jni", "native").any {
            name.lowercase().contains(it)
        }
    ) {
        enabled = false
    }
}
