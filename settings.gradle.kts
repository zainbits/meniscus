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

// Feature branch: the shared engine (liquid-core: glass renderer, shapes, springs), built from its
// checkout next to this one (~/git-apps/liquid-core) until it is published.
includeBuild("../liquid-core") {
    dependencySubstitution {
        substitute(module("io.github.zainbits:liquid-core")).using(project(":core"))
    }
}
