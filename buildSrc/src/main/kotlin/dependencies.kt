import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlinx(artifact: String, version: String? = null) =
    "org.jetbrains.kotlinx:kotlinx-$artifact${version?.let { ":$it" } ?: ""}"