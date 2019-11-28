object GlitchProject {
    const val repoSlug = "glitchlib/glitch"
    const val githubUrl = "https://github.com/$repoSlug"
    const val scmHttps = "scm:git:$githubUrl.git"
    const val scmSsh = "scm:git:git@github.com:$repoSlug.git"

    const val wikiUrl = "$githubUrl/wiki"
    const val ciUrl = "$githubUrl/actions"
    const val dlUrl = "$githubUrl/releases"
    const val mitLicense = "$githubUrl/blob/master/LICENCE.md"
    const val issues = "$githubUrl/issues"
    const val pullRequests = "$githubUrl/pulls"

    const val webUrl = "https://glitchlib.io"
}