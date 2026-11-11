import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import de.markusressel.kutepreferences.TARGET_SDK
import de.markusressel.kutepreferences.configureKotlinAndroid
import de.markusressel.kutepreferences.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            group = "de.markusressel.kutepreferences.library"

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            // Not on the lint configurations (androidLintTool and the like): lint brings its own
            // kotlin-reflect, which must match its kotlin-stdlib, and a forced newer stdlib makes lint
            // fail with "Can't initialize detector com.android.tools.lint.checks.InferredThreadDetector".
            configurations.matching { !it.name.startsWith("androidLint") }.configureEach {
                resolutionStrategy {
                    force(libs.findLibrary("kotlin-stdlib").get())
                    force(libs.findLibrary("kotlin-stdlib-jdk8").get())

                    force(libs.findLibrary("junit4").get())
                    // Temporary workaround for https://issuetracker.google.com/174733673
                    force("org.objenesis:objenesis:2.6")
                }
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}