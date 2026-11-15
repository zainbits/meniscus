plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "io.github.zainbits.meniscus"
    compileSdk = 36

    defaultConfig {
        // RuntimeShader (AGSL) arrived in Android 13.
        minSdk = 33
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = false)
    // Maven Central needs signed artifacts; local test publishes (publishToMavenLocal) do not.
    if (providers.gradleProperty("signingInMemoryKey").isPresent) signAllPublications()
    coordinates(
        groupId = providers.gradleProperty("GROUP").get(),
        artifactId = "meniscus",
        version = providers.gradleProperty("VERSION_NAME").get(),
    )

    pom {
        name.set("Meniscus")
        description.set("A Jetpack Compose bottom navigation bar whose selected tab pinches off like a liquid drop, drawn with an AGSL signed-distance shader.")
        inceptionYear.set("2026")
        url.set("https://github.com/zainbits/meniscus")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("zainbits")
                name.set("Zain Shaikh")
                url.set("https://github.com/zainbits")
            }
        }
        scm {
            url.set("https://github.com/zainbits/meniscus")
            connection.set("scm:git:git://github.com/zainbits/meniscus.git")
            developerConnection.set("scm:git:ssh://git@github.com/zainbits/meniscus.git")
        }
    }
}
