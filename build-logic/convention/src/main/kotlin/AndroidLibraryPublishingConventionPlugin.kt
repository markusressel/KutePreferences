import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

class AndroidLibraryPublishingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("maven-publish")

            extensions.apply {
                configure<LibraryExtension> {
                    publishing {
                        singleVariant("release") {
                            group = "com.github.markusressel.KutePreferences"
                            withJavadocJar()
                            withSourcesJar()
                        }
                    }
                }
                configure<PublishingExtension> {
                    publications {
                        create("maven", MavenPublication::class) {
                            groupId = "com.github.markusressel.KutePreferences"
                            artifactId = target.name
                            version = "${target.version}"

                            artifact("$buildDir/outputs/aar/${target.name}-release.aar") {
                                builtBy(tasks.getByName("assemble"))
                            }
                        }
                    }
                    repositories {
                        mavenLocal()
                    }
                }
            }
        }
    }
}