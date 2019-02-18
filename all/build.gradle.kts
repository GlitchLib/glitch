import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.gradle.DokkaTask

val projectList = rootProject.subprojects.filter {
    !arrayOf("bom", "auth", project.name, rootProject.name).contains(it.name) && !it.sourceSets.main.get().allSource.isEmpty
}.toSet()

dependencies {
    projectList.forEach {
        compile(it)
    }
}

tasks {
    sourceJar.get().enabled = false

    javadoc {
        isFailOnError = false
        title = "Gltich ${rootProject.version} API"

        options.delegateClosureOf<StandardJavadocDocletOptions> {
            windowTitle = "Glitch ${rootProject.version}"
            addStringOption("Xdoclint:none", "-quiet")
            isAuthor = true
        }
        projectList.forEach {
            val jdoc = it.tasks.getByName<Javadoc>("javadoc")

            source += jdoc.source
            classpath += jdoc.classpath
            excludes += jdoc.excludes
            includes += jdoc.includes
        }
    }

    dokka {
        sourceDirs = files(projectList.flatMap { it.sourceSets.main.get().allSource }.filter { it.name.endsWith(".kt") }.toList())
        classpath = files(projectList.flatMap { it.tasks.getByName<DokkaTask>("dokka").classpath }.toList())
    }
    
    withType<ShadowJar> {
        enabled = true
    }
}