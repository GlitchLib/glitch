## Repository
```groovy fct_label="Gradle"
repositories {
  jcenter()
  maven { url "https://dl.bintray.com/stachu540/GlitchLib" } // this repository is optional
}
```
```kotlin fct_label="Kotlin DSL"
repositories {
  jcenter()
  maven("https://dl.bintray.com/stachu540/GlitchLib") // this repository is optional
}
```

## With Dependency Managing `BOM` (Bills of Materials)

!!! info
    In Gradle 5.0+ you will not need a Spring Dependency Management, because in this version managed dependency has been implemented.
    Older versions needs to manage dependecy if you using `glitch-bom`.


### Gradle 5+

```groovy fct_label="Gradle"
dependencies {
  compile platform("io.glitchlib:glitch-bom:$glitch_version")
  compile "io.glitchlib:glitch-core"
  compile "io.glitchlib:glitch-kraken"
}
```

```kotlin fct_label="Kotlin DSL"
dependencies {
  compile(platform("io.glitchlib:glitch-bom:$glitch_version"))
  compile("io.glitchlib:glitch-core")
  compile("io.glitchlib:glitch-kraken")
}
```

### Gradle > 5.0

```groovy fct_label="Gradle"
plugins {
  id "io.spring.dependency-management" version "1.0.6.RELEASE"
}

dependencyManagement {
  imports {
    mavenBom "io.glitchlib:glitch-bom:$glitch_version"
  }
}

dependencies {
  compile "io.glitchlib:glitch-core"
  compile "io.glitchlib:glitch-kraken"
}
```

```kotlin  fct_label="Kotlin DSL"
plugins {
  id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

configure<DependencyManagementExtension> {
  imports {
    mavenBom("io.glitchlib:glitch-BOM:$glitch_version")
  }
}

dependencies {
  compile("io.glitchlib:glitch-core")
  compile("io.glitchlib:glitch-kraken")
}
```

## With all or specific required component

### `glitch-all`
```groovy fct_label="Gradle"
dependencies {
  compile "io.glitchlib:glitch-all:$glitch_version"
}
```

```kotlin fct_label="Kotlin DSL"
dependencies {
  compile("io.glitchlib:glitch-all:$glitch_version")
}
```

### Other

```groovy fct_label="Gradle"
dependencies {
  // this dependency is required for import other dependencies
  compile "io.glitchlib:glitch-core:$glitch_version"
  compile "io.glitchlib:glitch-kraken:$glitch_version"
}
```

```kotlin fct_label="Kotlin DSL"
dependencies {
  // this dependency is required for import other dependencies
  compile("io.glitchlib:glitch-core:$glitch_version")
  compile("io.glitchlib:glitch-kraken:$glitch_version")
}
```

