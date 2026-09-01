pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven(url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
}
rootProject.name = "SearchViewDialog"
include(":app")
include(":library")
