pluginManagement {
    includeBuild("build-logic")
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

// Core modules
include(":core:model")
include(":core:common")
include(":core:designsystem")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:database")
include(":core:datastore")

// Feature modules
include(":feature:home")
include(":feature:shop")
include(":feature:mypage")
include(":feature:tildetails")
include(":feature:editor")
include(":feature:report")
include(":feature:statistics")
