dependencies {
    base()
    `test-base`()
    api(project(":core"))
    compile("com.drewnoakes:metadata-extractor:2.11.0")
}