import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "de.markusressel.kutepreferences.buildlogic"

java {
    // Up to Java 11 APIs are available through desugaring
    // https://developer.android.com/studio/write/java11-minimal-support-table
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "kutepreferences.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "kutepreferences.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidHilt") {
            id = "kutepreferences.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "kutepreferences.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "kutepreferences.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryPublishing") {
            id = "kutepreferences.android.library.publishing"
            implementationClass = "AndroidLibraryPublishingConventionPlugin"
        }
        register("androidTest") {
            id = "kutepreferences.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidFlavors") {
            id = "kutepreferences.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
    }
}
