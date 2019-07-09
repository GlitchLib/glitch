tasks {
    javadoc.get().enabled = false
    dokka.get().enabled = false
    withType<Jar> {
        enabled = false
    }
    test.get().enabled = false
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                withXml {
                    asNode().appendNode("dependencyManagement").appendNode("dependencies").apply {
                        moduleList.map { project(":$it") }.forEach {
                            appendNode("dependency").apply {
                                appendNode("groupId", "${it.group}")
                                appendNode("artifactId", it.base.archivesBaseName)
                                appendNode("version", "${it.version}")
                            }
                        }
                    }
                }
            }
        }
    }
}