plugins {
    id("kutepreferences.android.library")
    id("kutepreferences.android.library.compose")
    id("kutepreferences.android.library.publishing")
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "de.markusressel.kutepreferences.ui"
}

dependencies {
    // types of core are part of the public API of ui
    api(project(":core"))

    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    api(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.foundation)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.animation.graphics)

    // KutePreferencesViewModel extends ViewModel, and viewModel { } is how consumers create it
    api(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)


    // color picker
    implementation(libs.godaddy.colorpicker)

    // shimmer effect
    implementation(libs.compose.shimmer)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
