plugins {
    `git-properties`
}

dependencies {
    core()
    `test-base`()
}

gitProperties {
    gitPropertiesDir = "$buildDir/resources/main/io/glitchlib"
    keys = listOf(
        "git.branch",
        "git.commit.id",
        "git.commit.id.abbrev",
        "git.commit.id.describe"
    )
    dateFormatTimeZone = "GMT"
    customProperty("application.name", rootProject.name)
    customProperty("application.version", rootProject.version)
    customProperty("application.url", ext.properties["url.project"])
    customProperty("application.git_url", "${ext.properties["url.github"]}.git")
    customProperty("application.description", rootProject.description)
}