plugins {
    id("kutepreferences.android.library")
    id("kutepreferences.android.library.compose")
    id("kutepreferences.android.library.publishing")
}

android {
    namespace = "de.markusressel.kutepreferences.ui"
}

dependencies {
    implementation(project(":core"))

    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.animation.graphics)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // dialogs
    implementation(libs.vanpra.compose.material.dialogs)
    implementation(libs.vanpra.compose.material.dialogs.datetime)

    // color picker
    implementation(libs.godaddy.colorpicker)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
