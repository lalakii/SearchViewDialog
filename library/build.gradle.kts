import com.android.build.gradle.internal.tasks.AarMetadataTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    `maven-publish`
    signing
    id("cn.lalaki.central") version "2.0.2"
}

android {
    namespace = "cn.lalaki.dialog"
    compileSdk = 36
    version = 3.2
    defaultConfig {
        minSdk = 21
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
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    buildToolsVersion = "36.1.0"
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("cn.lalaki:pinyin4j-chinese-simplified:1.0.7")
}
configurations.all {
    exclude("androidx.profileinstaller", "profileinstaller")
}
tasks.withType<AarMetadataTask> {
    isEnabled = false
}
tasks.configureEach {
    if (name.contains("checkDebugAndroidTestAarMetadata")) {
        enabled = false
    }
}
centralPortalPlus{
    tokenXml = uri("D:\\BIN\\token.txt")
}
publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("D:\\repo\\")
        }
    }
    publications {
        create<MavenPublication>("release") {
            artifactId = "SearchViewDialog"
            groupId = "cn.lalaki"
            afterEvaluate { artifact(tasks.named("bundleReleaseAar")) }
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.implementation.get().dependencies.forEach { dependency ->
                    if (dependency.version != "unspecified" && dependency.name != "unspecified") {
                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dependency.group)
                        dependencyNode.appendNode("artifactId", dependency.name)
                        dependencyNode.appendNode("version", dependency.version)
                    } else {
                        println(">>> [WARN] Excluded module: " + dependency.group + ":" + dependency.name)
                    }
                }
            }
            pom {
                name = "SearchViewDialog"
                description = "A simple dialog box that supports pinyin search and multi-selection."
                url = "https://github.com/lalakii/SearchViewDialog"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        name = "lalakii"
                        email = "i@lalaki.cn"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/lalakii/SearchViewDialog.git"
                    developerConnection = "scm:git:https://github.com/lalakii/SearchViewDialog.git"
                    url = "https://github.com/lalakii/SearchViewDialog"
                }
            }
        }
    }
}
signing {
    useGpgCmd()
    sign(publishing.publications["release"])
}
