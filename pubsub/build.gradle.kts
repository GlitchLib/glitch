dependencies {
    compileOnly(project(":core"))
    compileOnly(project(":kraken"))
    compileOnly(project(":helix"))
    testCompile(project(":core"))
    testCompile(project(":kraken"))
    testCompile(project(":helix"))
}