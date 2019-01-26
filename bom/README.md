# Glitch BOM (Bills of Materials)

This module provides the flexibility to add a dependency to our module without worrying about the version that we should depend on.

## Gradle

```groovy
plugins {
  id "io.spring.dependency-management" version "1.0.6.RELEASE"
}

repositories {
  jcenter()
  maven { url "https://dl.bintray.com/stachu540/GlitchLib" }
}

dependencyManagement {
  imports {
    mavenBom "io.glitchlib:glitch-BOM:$glitch_version"
  }
}

dependencies {
  compile "io.glitchlib:glitch-core"
  compile "io.glitchlib:glitch-kraken"
}

```

## Gradle Kotlin DSL

```kotlin
plugins {
  id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

repositories {
  jcenter()
  maven("https://dl.bintray.com/stachu540/GlitchLib")
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

## Maven

```xml
<project>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.glitchlib</groupId>
                <artifactId>glitch-BOM</artifactId>
                <version>${glitch.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
   <dependencies>
       <dependency>
           <groupId>io.glitchlib</groupId>
           <artifactId>glitch-core</artifactId>
       </dependency>
       <dependency>
           <groupId>io.glitchlib</groupId>
           <artifactId>glitch-kraken</artifactId>
       </dependency>
   </dependencies>
</project>
```
