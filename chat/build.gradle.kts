dependencies {
    compileOnly(project(":core"))
    compileOnly(project(":kraken"))

    testCompile(project(":core"))
    testCompile(project(":kraken"))
}