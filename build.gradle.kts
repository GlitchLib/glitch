import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Date

buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Version.`android-plugin`}")
    }
}

plugins {
    kotlin("multiplatform") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.61"
    id("base")
    id("maven-publish")
    id("com.jfrog.bintray") version Version.bintray
}

allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.bintray")

    kotlin {
        jvm()
        js()
        plugins.withId("com.android.library") {
            android()
        }
        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(kotlin("stdlib-common"))
                    implementation("io.ktor:ktor-client-core:${Version.ktor}")
                    implementation("io.ktor:ktor-client-serialization:${Version.ktor}")
                    implementation("io.ktor:ktor-client-websockets:${Version.ktor}")
                    implementation("io.ktor:ktor-client-logging:${Version.ktor}")
                }
            }
            val commonTest by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                    implementation("io.ktor:ktor-client-mock:${Version.ktor}")
                }
            }
            // JDK
            val jvmMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                    implementation("io.ktor:ktor-client-okhttp:${Version.ktor}")
                    implementation("io.ktor:ktor-client-serialization-jvm:${Version.ktor}")
                    implementation("io.ktor:ktor-client-websockets-jvm:${Version.ktor}")
                    implementation("io.ktor:ktor-client-logging-jvm:${Version.ktor}")
                }

            }
            val jvmTest by getting {
                dependencies {
                    dependsOn(jvmMain)
                    dependsOn(commonTest)
                    implementation(kotlin("test-junit5"))
                    implementation("io.ktor:ktor-client-mock-jvm:${Version.ktor}")
                }
            }
            // JavaScript
            val jsMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(kotlin("stdlib-js"))
                    implementation("io.ktor:ktor-client-js:${Version.ktor}")
                    implementation("io.ktor:ktor-client-serialization-js:${Version.ktor}")
                    implementation("io.ktor:ktor-client-websockets-js:${Version.ktor}")
                    implementation("io.ktor:ktor-client-logging-js:${Version.ktor}")
                }
            }
            val jsTest by getting {
                dependsOn(jsMain)
                dependsOn(commonTest)
                dependencies {
                    implementation(kotlin("test-js"))
                    implementation("io.ktor:ktor-client-mock-js:${Version.ktor}")
                }
            }
            // Android
            plugins.withId("com.android.library") {
                val androidMain by getting {
                    dependsOn(jvmMain)
                    dependencies {
                        implementation(kotlin("stdlib"))
                        implementation("io.ktor:ktor-client-android:${Version.ktor}")
                        implementation("io.ktor:ktor-client-serialization-jvm:${Version.ktor}")
                        implementation("io.ktor:ktor-client-logging-jvm:${Version.ktor}")
                    }
                }
                val androidTest by getting {
                    dependsOn(androidMain)
                    dependsOn(commonTest)
                    dependsOn(jvmTest)
                    dependencies {
                        implementation(kotlin("test-junit5"))
                        implementation("io.ktor:ktor-client-mock-jvm:${Version.ktor}")
                    }
                }
            }
        }
    }

    bintray {
        user = bintrayUser
        key = bintrayApiKey
        publish = false
        pkg.apply {
            userOrg = bintrayUser
            repo = "GlitchLib"
            name = "Glitch"
            desc = "Multiplatform API Wrapper for Twitch in Kotlin"
            setLicenses("MIT")
            publicDownloadNumbers = true
            vcsUrl = GlitchProject.githubUrl + ".git"
            publicDownloadNumbers = true
            version.apply {
                name = "$version"
                vcsTag = "v${version}"
                released = "${Date()}"
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "5.6.2"
        distributionType = Wrapper.DistributionType.ALL
    }
}

afterEvaluate {
    allprojects.forEach { p ->
        if (!p.projectDir.exists()) p.projectDir.mkdirs()
        if (p.name !in arrayOf("bom", "all")) {
            if (p.plugins.hasPlugin("kotlin-multiplatform")) {
                p.sourceSets.forEach {
                    it.allSource.sourceDirectories.forEach {
                        if (!it.exists()) {
                            it.mkdirs()
                        }
                    }
                }
            }
        }
    }
}