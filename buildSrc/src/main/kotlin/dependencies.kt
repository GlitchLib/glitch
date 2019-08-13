import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.kotlin

fun DependencyHandler.core() {
    base()
    add("compile", "io.reactivex.rxjava2:rxjava:${Versions.`rx-java`}")
    add("compile", "io.reactivex.rxjava2:rxkotlin:${Versions.`rx-kotlin`}")

    add("compile", "com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    add("compile", "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}")
    add("compile", "com.google.code.gson:gson:${Versions.gson}")
}

fun DependencyHandler.base() {
    add("compile", kotlin("reflect"))
    add("compile", kotlin("stdlib-jdk8"))
    add("compile", "org.slf4j:slf4j-api:${Versions.slf4j}")
    add("api", "io.reactivex.rxjava2:rxandroid:${Versions.`rx-android`}")
}

fun DependencyHandler.`test-base`() {
    add("testCompile", kotlin("test-junit5"))
    add("testCompile", "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}")
    add("testRuntimeOnly", "org.spekframework.spek2:spek-runner-junit5:${Versions.spek}")
}