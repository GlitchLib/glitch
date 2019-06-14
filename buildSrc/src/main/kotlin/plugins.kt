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

val PluginDependenciesSpec.`dependency-management`
    get() = id("io.spring.dependency-management") version "1.0.6.RELEASE"