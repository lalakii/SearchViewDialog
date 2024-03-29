pluginManagement {
    repositories {
        maven(url = "https://jitpack.io")
        maven(url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://jitpack.io")
        maven(url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
}
rootProject.name = "SearchViewDialog"
include(":app")
include(":library")