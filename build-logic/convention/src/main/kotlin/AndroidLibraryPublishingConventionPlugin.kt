import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

class AndroidLibraryPublishingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("maven-publish")

            group = PUBLICATION_GROUP

            extensions.configure<LibraryExtension> {
                publishing {
                    singleVariant("release") {
                        withJavadocJar()
                        withSourcesJar()
                    }
                }
            }

            // the "release" software component is only available after evaluation
            afterEvaluate {
                extensions.configure<PublishingExtension> {
                    publications {
                        create("maven", MavenPublication::class) {
                            groupId = PUBLICATION_GROUP
                            artifactId = target.name
                            version = "${target.version}"

                            // includes the dependencies of the module in the published POM
                            from(components["release"])

                            versionMapping {
                                allVariants {
                                    fromResolutionResult()
                                }
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

    private companion object {
        const val PUBLICATION_GROUP = "com.github.markusressel.KutePreferences"
    }
}
