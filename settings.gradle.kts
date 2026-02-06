pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Tilly"
include(":app")
includeBuild("build-logic")

// Core modules
include(":core:model")
include(":core:common")
include(":core:designsystem")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:navigation")

// Feature modules
include(":feature:home")
include(":feature:shop")
include(":feature:tildetails")
include(":feature:editor")
include(":feature:report")
include(":feature:statistics")
