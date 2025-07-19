plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "org.sandw.core.datastore.proto"
    compileSdk = 35

    sourceSets {
        getByName("main") {

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents.beforeVariants {
    android.sourceSets.getByName(it.name) {
        val buildDir = layout.buildDirectory.get().asFile
        java {
            srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
        }
        kotlin {
            srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
        }
    }
}

dependencies {
    api(libs.protobuf.kotlin.lite)
}
