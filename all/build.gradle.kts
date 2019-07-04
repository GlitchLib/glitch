import org.jetbrains.dokka.gradle.DokkaTask

val projectList = moduleList.map { project(":$it") }

dependencies {
    projectList.forEach {
        compile(it)
    }
}

tasks {
    create<DokkaTask>("mkdokka") {
        jdkVersion = 8
        moduleName = "api"
        outputFormat = "mkdocs"
        skipEmptyPackages = true
        outputDirectory = File(buildDir, "docs/mkdokka").absolutePath
        val sourceSets: Collection<SourceSet> = projectList.map { it.sourceSets.getByName("main") }
        sourceDirs = files(sourceSets.map { it.allSource })
        classpath = files(sourceSets.map { it.runtimeClasspath })
        dokkaFatJar = "com.github.stachu540:dokka-mkdocs-fatjar:0.1.0"
        doLast {
            copy {
                from(this@create.outputDirectory)
                into(File(rootProject.rootDir, "docs"))
            }
        }
    }
}