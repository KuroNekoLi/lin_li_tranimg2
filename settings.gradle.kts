pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "http://192.168.99.70:8081/repository/maven-public/") {
            isAllowInsecureProtocol = true
        }
    }
}

rootProject.name = "lin_li_tranimg"
include(":app")
 