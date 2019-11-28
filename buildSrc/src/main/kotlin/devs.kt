import org.gradle.api.publish.maven.MavenPomDeveloperSpec

val MavenPomDeveloperSpec.stachu540
    get() = developer {
        name.set("Damian Staszewski")
        url.set("https://github.com/stachu540")
        timezone.set("Europe/Warsaw")
        roles.add("Owner/Creator")
    }