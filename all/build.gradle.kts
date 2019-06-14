val projectList = moduleList.map { project(":$it") }

dependencies {
    projectList.forEach {
        compile(it)
    }
}

tasks {
    dokka {
        val sourceSets: Collection<SourceSet> = projectList.map { it.sourceSets.getByName("main") }
        sourceDirs = files(sourceSets.map { it.allSource })
        classpath = files(sourceSets.map { it.runtimeClasspath })
    }
}