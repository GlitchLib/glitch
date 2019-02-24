import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*

plugins {
    jacoco
    `maven-publish`
    kotlin("jvm") version "1.3.21"
    id("com.jfrog.bintray") version "1.8.4"
    id("org.jetbrains.dokka") version "0.9.17"
    id("com.github.ben-manes.versions") version "0.20.0"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    id("com.gorylenko.gradle-git-properties") version "2.0.0" apply false
}

val timestamp = SimpleDateFormat("MMM dd yyyy HH:mm:ss zzz", Locale.ENGLISH)
        .apply {
            setTimeZone(TimeZone.getTimeZone("GMT"))
        }.format(Date())

val bintrayUser = System.getenv("BINTRAY_USER") ?: findProperty("bintray.user").toString()
val bintrayApiKey = System.getenv("BINTRAY_API_KEY") ?: findProperty("bintray.api_key").toString()
val sonatypeUser = System.getenv("CENTRAL_USER") ?: findProject("central.user").toString()
val sonatypePassword = System.getenv("CENTRAL_PASSWORD") ?: findProject("central.password").toString()
val githubToken = System.getenv("GITHUB_TOKEN") ?: findProject("github.token").toString()

allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    val module = project.projectDir.canonicalPath.replace(rootProject.projectDir.canonicalPath, "").substring(1).replace('\\', '-')

    base.archivesBaseName = "${rootProject.name.toLowerCase()}-$module"
    if (base.archivesBaseName.contains("auth")) {
        group = "io.glitchlib.auth"
    }

    dependencies {
        if (!arrayOf("bom", "all", "auth", rootProject.name).contains(project.name)) {
            // https://docs.gradle.org/5.0/userguide/managing_transitive_dependencies.html#sec:bom_import
            compile(enforcedPlatform("io.projectreactor:reactor-bom:Californium-SR5"))

            compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            compile("org.jetbrains.kotlin:kotlin-reflect")

            compile("org.slf4j:slf4j-api:1.7.25")

            testCompile("ch.qos.logback:logback-classic:1.2.3")

            testCompile("io.projectreactor:reactor-test")
        }
    }

    val dokka by tasks.getting(DokkaTask::class) {
        moduleName = module
        jdkVersion = 8
    }

    val javadoc by tasks.getting(Javadoc::class) {
        options {
            encoding = "UTF-8"
        }
        exclude("**/*Impl.java", "**/GsonAdapters*.java")
    }

    val sourceJar by tasks.creating(Jar::class) {
        dependsOn(tasks.getByName("classes"))
        description = "Builds the sources jar."
        group = "build"
        from(sourceSets.main.get().allSource)
        classifier = "sources"
    }

    val javadocJar by tasks.creating(Jar::class) {
        dependsOn(javadoc)
        description = "Builds the JavaDoc jar."
        group = "build"
        from(javadoc.destinationDir)
        classifier = "javadoc"

    }

    val kdocJar by tasks.creating(Jar::class) {
        dependsOn(dokka)
        description = "Builds the KotlinDoc jar."
        group = "build"
        from(dokka.outputDirectory)
        classifier = "kdoc"
    }
    val shadowJar by tasks.getting(ShadowJar::class) {
        classifier = "shaded"
    }

    val dependecySize by tasks.creating {
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

    if (project.name != "bom") {
        artifacts {
            if (project.name != "all") {
                add("archives", sourceJar)
            }
            add("archives", shadowJar)
            add("archives", javadocJar)
            add("archives", kdocJar)
        }
    }

    publishing {
        publications {
            register("maven", MavenPublication::class) {
                artifactId = base.archivesBaseName
                if (project.name != "bom") {
                    if (project.name != "all") {
                        from(components["java"])
                        artifact(sourceJar)
                    }
                    artifact(shadowJar)
                    artifact(javadocJar)
                    artifact(kdocJar)
                }
                pom {
                    url.set("https://glitchlib.github.io/")
                    issueManagement {
                        system.set("GitHub")
                        url.set("https://github.com/glitchlib/glitch/issues")
                    }
                    ciManagement {
                        system.set("Travis-CI")
                        url.set("https://travis-ci.com/GlitchLib/glitch")
                    }
                    inceptionYear.set("2018")
                    developers {
                        developer {
                            id.set("stachu540")
                            name.set("Damian Staszewski")
                            url.set("https://github.com/stachu540")
                            timezone.set("Europe/Warsaw")
                        }
                    }
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://github.com/GlitchLib/glitch/blob/master/LICENCE.md")
                            distribution.set("repo")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/glitchlib/glitch.git")
                        developerConnection.set("scm:git:git@github.com:glitchlib/glitch.git")
                        url.set("https://github.com/glitchlib/glitch")
                    }
                    distributionManagement {
                        downloadUrl.set("https://github.com/glitchlib/glitch/releases")
                    }
                }
            }
        }
    }

    if (sourceSets.main.get().allSource.isEmpty && !arrayOf("bom", "all").contains(project.name)) {
        tasks.findByName("bintrayUpload")?.enabled = false
        tasks.findByName("bintrayPublish")?.enabled = false
    }

    bintray {
        user = bintrayUser
        key = bintrayApiKey
        setPublications("maven")
        dryRun = false
        publish = true
        override = false
        pkg.apply {
            userOrg = bintrayUser
            repo = "GlitchLib"
            name = "Glitch"
            desc = "Java API Wrapper for Twitch"
            setLicenses("MIT")
            publicDownloadNumbers = true
            vcsUrl = "https://github.com/GlitchLib/glitch.git"
            version.apply {
                name = rootProject.version.toString()
                vcsTag = "v${rootProject.version}"
                released = Date().toString()
            }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        withType<JavaCompile> {
            options.apply {
                isIncremental = true
                encoding = "UTF-8"
            }
        }
        withType<KotlinCompile> {
            incremental = true
            kotlinOptions.jvmTarget = "1.8"
        }
        withType<Jar> {
            manifest {
                attributes(
                        "Manifest-Version" to "1.0",
                        "Created-By" to "Gradle ${gradle.gradleVersion} - JDK ${System.getProperty("java.specification.version")} (${System.getProperty("java.version")})",
                        "Implementation-Title" to rootProject.name,
                        "Implementation-Vendor" to base.archivesBaseName,
                        "Implementation-Version" to version,
                        "Implementation-Date" to timestamp
                )
            }
        }

        withType<Test> {
            shouldRunAfter(dependecySize)
        }
        withType<DokkaTask> {
            skipEmptyPackages = true
            outputDirectory = File(buildDir, "docs/dokka").absolutePath
        }
    }
}

rootProject.apply {
    tasks.findByName("bintrayUpload")?.enabled = false
    tasks.findByName("bintrayPublish")?.enabled = false
}

tasks {
    withType<Wrapper> {
        gradleVersion = "5.2.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    create("publishRelease") {
        group = "publishing"
        dependsOn(allprojects.map { it.tasks.getByName("shadowJar", ShadowJar::class) }.toList())

        doLast {
            val dest = File(rootProject.buildDir, "release")

            allprojects.filter {
                (!it.sourceSets.main.get().allSource.isEmpty || it.name == "all") &&
                        (it.tasks.javadoc.get().destinationDir!!.exists() || File(it.tasks.dokka.get().outputDirectory).exists()) &&
                        !arrayOf("bom", "auth", rootProject.name).contains(it.name)
            }.forEach {
                val shadow = it.tasks.getByName("shadowJar", ShadowJar::class)
                val file = File(shadow.destinationDir, shadow.archiveName)
                if (file.exists() && file.isFile && file.name.endsWith(".jar")) {
                    copy {
                        from(file)
                        into(dest)
                    }
                }
            }
        }
    }

    create("publishDoc") {
        group = "publishing"
        dependsOn(allprojects.flatMap { arrayOf(it.tasks.javadoc.get(), it.tasks.dokka.get()).asIterable() }.toList<Task>())

        doLast {
            val dest = File(rootProject.buildDir, "docs")

            allprojects.filter {
                (!it.sourceSets.main.get().allSource.isEmpty || it.name == "all") &&
                        (it.tasks.javadoc.get().destinationDir!!.exists() || File(it.tasks.dokka.get().outputDirectory).exists()) &&
                        !arrayOf("bom", "auth", rootProject.name).contains(it.name)
            }.forEach {
                val name = it.projectDir.canonicalPath.replace(rootProject.projectDir.canonicalPath, "").substring(1).replace('\\', '-')

                copy {
                    from(it.tasks.javadoc.get().destinationDir)
                    into(File(dest, "javadoc/$name"))
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                }

                copy {
                    from(it.tasks.dokka.get().outputDirectory)
                    into(File(dest, "kdoc"))
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                }
            }
        }
    }
}