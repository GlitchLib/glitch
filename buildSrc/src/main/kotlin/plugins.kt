import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.bintray
    get() = id("com.jfrog.bintray") version Versions.bintray

val PluginDependenciesSpec.artifactory
    get() = id("com.jfrog.artifactory") version Versions.artifactory

val PluginDependenciesSpec.`kotlin-jvm`
    get() = kotlin("jvm") version Versions.kotlin

val PluginDependenciesSpec.versions
    get() = id("com.github.ben-manes.versions") version Versions.versions

val PluginDependenciesSpec.shadow
    get() = id("com.github.johnrengelman.shadow") version Versions.shadow

val PluginDependenciesSpec.`git-properties`
    get() = id("com.gorylenko.gradle-git-properties") version Versions.`git-properties`

val PluginDependenciesSpec.dokka
    get() = id("org.jetbrains.dokka") version Versions.dokka

fun PluginDependenciesSpec.basePlugins() {
    `maven-publish`
    bintray
    dokka
    artifactory
    `kotlin-jvm`
    shadow
}

fun PluginDependenciesSpec.baseWithAndroid() {
    basePlugins()
    id("com.android.library")
}

fun PluginDependenciesSpec.topPlugins() {
    `maven-publish`
    versions
}