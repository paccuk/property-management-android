import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.stkachenko.propertymanagement.convention.configureKotlinJvm
import org.stkachenko.propertymanagement.convention.configureSpotlessForJvm
import org.stkachenko.propertymanagement.convention.libs

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "propertymanagement.android.lint")

            configureKotlinJvm()
            configureSpotlessForJvm()
            dependencies {
                "testImplementation"(libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
