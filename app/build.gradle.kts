plugins {
    id("kutepreferences.android.application")
    id("kutepreferences.android.application.compose")
    id("kutepreferences.android.application.flavors")
    id("kutepreferences.android.hilt")
}

android {
    defaultConfig {
        applicationId = "de.markusressel.kutepreferences.demo"
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "KutePreferences_v${versionName}_(${versionCode})")
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.addAll(
                listOf("LICENSE.txt", "META-INF/DEPENDENCIES", "META-INF/ASL2.0", "META-INF/NOTICE", "META-INF/LICENSE")
            )
            pickFirsts.add("META-INF/proguard/androidx-annotations.pro")
        }
    }

    namespace = "de.markusressel.kutepreferences.demo"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ui"))

    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.android.material)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // required because of this: https://stackoverflow.com/questions/70894168/targeting-s-version-31-and-above-requires-that-one-of-flag-immutable-or-flag
    implementation(libs.androidx.work.runtime.ktx)

    // Hilt (Dagger wrapper)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // For instrumentation tests
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    // For local unit tests
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)

    // Hilt Jetpack integration
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    //the core iconcis library (without any widgets)
    implementation(libs.iconics.core)
    //this adds all ui view widgets (IconicsButton, IconicsImageView, ...)
    implementation(libs.iconics.views)
    implementation(libs.iconics.compose)
    implementation(libs.iconics.typefaceapi)
    implementation(libs.iconics.typeface.material)

    implementation(libs.androidx.appcompat)

    // Animations
    implementation(libs.androidx.compose.animation)


    implementation(libs.androidx.compose.runtime)
    // Compose
    implementation(libs.androidx.compose.ui)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.androidx.compose.foundation)
    // Material Design
    implementation(libs.androidx.compose.material)
    // Integration with activities
    implementation(libs.androidx.activity.compose)
    // Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // When using a MDC theme
    implementation(libs.android.material.compose.theme.adapter)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}