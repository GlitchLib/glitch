import org.gradle.api.Project

val Project.bintrayUser: String
    get() = System.getenv("BINTRAY_USER") ?: findProperty("bintray.user").toString()

val Project.bintrayApiKey: String
    get() = System.getenv("BINTRAY_API_KEY") ?: findProperty("bintray.api_key").toString()

val Project.githubToken: String
    get() = System.getenv("GITHUB_TOKEN") ?: findProject("github.token").toString()

val Project.isSnapshot: Boolean
    get() = (rootProject.version as String).endsWith("-SNAPSHOT")

val moduleList = arrayOf("core", "chat", "kraken", "helix", "pubsub", "webhook")