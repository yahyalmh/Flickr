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
    }
}
rootProject.name = "Flickr"
include( ":app")
include( ":ui:main")
include( ":ui:common")
include( ":ui:home")
include( ":ui:search")
include( ":ui:detail")
include( ":data:common")
include(":data:flickr")
include(":data:bookmark")
include(":data:searchhistory")
