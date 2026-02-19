import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.stkachenko.propertymanagement.convention.libs

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "propertymanagement.android.library")
            apply(plugin = "propertymanagement.hilt")

            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))

                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())

                "androidTestImplementation"(
                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get()
                )
            }
        }
    }
}
