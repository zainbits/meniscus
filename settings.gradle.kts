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

rootProject.name = "meniscus"
include(":meniscus")
include(":sample")

// Feature branch: the shared engine (Cohesion: glass renderer, shapes, springs), built from its
// checkout next to this one (~/git-apps/cohesion) until it is published.
includeBuild("../cohesion") {
    dependencySubstitution {
        substitute(module("io.github.zainbits:cohesion")).using(project(":cohesion"))
    }
}
