plugins {
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

description = "Bills of Materials (BOM) for Glitch packages"

tasks {
    compileJava.get().enabled = false
    javadoc.get().enabled = false
    withType<Jar> {
        enabled = false
    }
    test.get().enabled = false
}

dependencyManagement {
    dependencies {
        rootProject.subprojects.forEach {
            if (!arrayOf("bom", "all").contains(it.name) && !it.sourceSets.main.get().allSource.isEmpty) {
                dependency("${it.group}:${it.base.archivesBaseName}:${it.version}")
            }
        }
    }
}

