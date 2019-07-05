import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.GroovyObject
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import java.util.*

plugins {
    dokka
    versions
    bintray
    artifactory
    `kotlin-jvm`
    `maven-publish`
    shadow apply false
}

val bintrayUser = System.getenv("BINTRAY_USER") ?: findProperty("bintray.user").toString()
val bintrayApiKey = System.getenv("BINTRAY_API_KEY") ?: findProperty("bintray.api_key").toString()
val sonatypeUser = System.getenv("CENTRAL_USER") ?: findProject("central.user").toString()
val sonatypePassword = System.getenv("CENTRAL_PASSWORD") ?: findProject("central.password").toString()


allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.jfrog.artifactory")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    val sourceJar by tasks.creating(Jar::class) {
        dependsOn(tasks.getByName("classes"))
        description = "Builds the sources jar."
        group = "build"
        from(sourceSets.main.get().allSource)
        classifier = "sources"
    }

    val javadocJar by tasks.creating(Jar::class) {
        dependsOn(tasks.dokka)
        description = "Builds the KotlinDoc jar."
        group = "build"
        from(tasks.dokka.get().outputDirectory)
        classifier = "javadoc"
    }

    val shadowJar by tasks.getting(ShadowJar::class) {
        classifier = "shaded"
    }

    if (project.name != "bom") {
        artifacts {
            if (project.name != "all") {
                archives(sourceJar)
            }
            archives(shadowJar)
            archives(javadocJar)
        }
    }

    sourceSets.getByName("main").runtimeClasspath

    publishing {
        publications {
            register<MavenPublication>("glitch") {
                artifactId = base.archivesBaseName
                if (project.name != "bom") {
                    if (project.name != "all") {
                        from(components["kotlin"])
                        artifact(sourceJar)
                    }
                    artifact(shadowJar)
                    artifact(javadocJar)
                }
                pom.default()
            }
        }
    }

    bintray {
        user = bintrayUser
        key = bintrayApiKey
        setPublications("glitch")
        dryRun = false
        publish = true
        override = false
        pkg.apply {
            userOrg = bintrayUser
            repo = GlitchProject.repoSlug.split('/')[0]
            name = GlitchProject.repoSlug.split('/')[1]
            desc = rootProject.description
            setLicenses("MIT")
            publicDownloadNumbers = true
            vcsUrl = GlitchProject.githubUrl + ".git"
            version.apply {
                name = "${rootProject.version}"
                vcsTag = "v${rootProject.version}"
                released = "${Date()}"
            }
        }
    }

    artifactory {
        setContextUrl("https://oss.jfrog.org/")
        publish(closureOf<PublisherConfig> {
            repository(closureOf<GroovyObject> {
                setProperty("repoKey", "oss-snapshot-local")
                setProperty("username", bintrayUser)
                setProperty("password", bintrayApiKey)
            })
            defaults(delegateClosureOf<GroovyObject> {
                invokeMethod("publications", "glitch")
            })
        })
    }

    tasks {
        val dependecySize by creating {
            if (arrayOf("bom", "auth").contains(project.name)) {
                enabled = false
            }
            doLast {
                var size = 0
                val formatStr = "%,10.2f"
                configurations.default.get().map { it.length() / (1024 * 1024) }.forEach { size.plus(it) }

                val out = StringBuffer()
                out.append("Total dependencies size:".padEnd(45))
                        .append("${String.format(formatStr, size)} MiB\n\n")

                configurations
                        .default
                        .get()
                        .sortedBy { -it.length() }
                        .forEach {
                            out.append(it.name.padEnd(45))
                                    .append("${String.format(formatStr, (it.length() / 1024))} KiB\n")
                        }
                println(out)
            }
        }

        dokka {
            jdkVersion = 8
            outputFormat = "javadoc"
            skipEmptyPackages = true
            outputDirectory = File(buildDir, "docs/dokka").absolutePath
        }

        test {
            shouldRunAfter(dependecySize)
            useJUnitPlatform {
                includeEngines("spek2")
            }
        }

        if (sourceSets.main.get().allSource.isEmpty && !arrayOf("bom", "all").contains(project.name)) {
            bintrayUpload.get().enabled = false
            artifactoryPublish.get().enabled = false
            publishToMavenLocal.get().enabled = false
        }

        publish {
            dependsOn(if (isSnapshot) artifactoryPublish else bintrayUpload)
        }

        withType<KotlinCompile> {
            incremental = true
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}

tasks {
    withType<Wrapper> {
        gradleVersion = "5.5"
        distributionType = Wrapper.DistributionType.ALL
    }
}