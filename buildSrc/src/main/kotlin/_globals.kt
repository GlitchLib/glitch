import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.util.Path

val Project.bintrayUser: String
    get() = System.getenv("BINTRAY_USER") ?: findProperty("bintray.user").toString()

val Project.bintrayApiKey: String
    get() = System.getenv("BINTRAY_API_KEY") ?: findProperty("bintray.api_key").toString()

val Project.githubToken: String
    get() = System.getenv("GITHUB_TOKEN") ?: findProject("github.token").toString()

val Project.isSnapshot: Boolean
    get() = (rootProject.version as String).endsWith("-SNAPSHOT")

val Project.artifactId : String
    get() = (this as DefaultProject).identityPath.path.replace(Path.SEPARATOR, "-").let {
        if (it.startsWith("-") && it.length > 1) "glitch$it" else "glitch"
    }
