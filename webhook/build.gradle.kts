dependencies {
    compileOnly(project(":core"))
    compileOnly(project(":helix"))

    testCompile(project(":core"))
    testCompile(project(":helix"))
}