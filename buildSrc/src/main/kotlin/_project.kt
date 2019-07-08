object GlitchProject {
    const val webUrl = "https://glitchlib.io"
    const val repoSlug = "GlitchLib/Glitch"
    val githubUrl = "https://github.com/$repoSlug"
    val ciUrl = "https://travis-ci.com/$repoSlug"
    val scmHttps = "scm:git:$githubUrl.git"
    val scmSsh = "scm:git:git@github.com:$repoSlug.git"

    val dlUrl = "$githubUrl/releases"
    val mitLicense = "$githubUrl/blob/master/LICENCE.md"
    val issues = "$githubUrl/issues"
    val pullRequests = "$githubUrl/pulls"

    val apiDocumentation = "$webUrl/api/latest"
}