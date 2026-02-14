import org.gradle.api.Plugin
import org.gradle.api.Project
import org.stkachenko.propertymanagement.convention.configureGraphTasks
import org.stkachenko.propertymanagement.convention.configureSpotlessForRootProject

class RootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        require(target.path == ":")
        target.subprojects { configureGraphTasks() }
        target.configureSpotlessForRootProject()
    }
}
