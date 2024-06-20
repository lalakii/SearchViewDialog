pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
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
