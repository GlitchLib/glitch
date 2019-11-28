import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicenseSpec

fun MavenPom.default() {
    url.set(GlitchProject.githubUrl)
    issueManagement {
        system.set("GitHub")
        url.set(GlitchProject.issues)
    }
    ciManagement {
        system.set("GitHub Actions")
        url.set(GlitchProject.ciUrl)
    }
    inceptionYear.set("2019")
    developers { all }
    licenses { mit("repo", GlitchProject.mitLicense) }
    scm {
        connection.set(GlitchProject.scmHttps)
        developerConnection.set(GlitchProject.scmSsh)
        url.set(GlitchProject.githubUrl)
    }
    distributionManagement { downloadUrl.set(GlitchProject.dlUrl) }
}

fun MavenPomLicenseSpec.mit(distribution: String, url: String) {
    license {
        name.set("MIT Licence")
        this.distribution.set(distribution)
        this.url.set(url)
    }
}

val MavenPomDeveloperSpec.all: Unit
    get() {
        stachu540
    }