pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Enable type-safe project accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Flickr"
include( ":app")
include( ":ui:main")
include( ":ui:common")
include( ":ui:home")
include( ":ui:search")
include( ":ui:detail")
include( ":data:common")
include(":data:bookmark")
include(":data:history")
include(":data:search")
include(":data:detail")
include(":ui:guide")
