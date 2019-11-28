plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(28)
}

dependencies {
    api(project(":core"))
}