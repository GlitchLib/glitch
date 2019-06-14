plugins {
    `dependency-management`
}

tasks {
    javadoc.get().enabled = false
    dokka.get().enabled = false
    withType<Jar> {
        enabled = false
    }
    test.get().enabled = false
}

dependencyManagement {
    dependencies {
        moduleList.map { project(":$it") }.forEach {
            dependency("${it.group}:${it.base.archivesBaseName}:${it.version}")
        }
    }
}