plugins {
    id("kutepreferences.android.library")
    id("kutepreferences.android.library.publishing")
}

android {
    namespace = "de.markusressel.kutepreferences.core"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib.jdk8)

    // StateFlow is part of the public API of core
    api(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.compose.ui.text)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.commons.validator)

    implementation(libs.gson)

    implementation(libs.androidx.datastore.preferences)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
