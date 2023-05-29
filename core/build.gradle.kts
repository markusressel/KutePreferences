plugins {
    id("kutepreferences.android.library")
    id("kutepreferences.android.library.publishing")
}

android {
    namespace = "de.markusressel.kutepreferences.core"
}

dependencies {
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.compose.ui.text)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.commons.validator)

    implementation(libs.gson)
    implementation(libs.markusressel.typedpreferences)

    implementation(libs.androidx.datastore.preferences)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
