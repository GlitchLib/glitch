# Glitch Core Package

This is a core package maintained from all modules in one place.

# Dependencies

## Gradle

### Development Build
```groovy
repositories {
    maven { url "https://jitpack.io/" }
}

dependencies {
    compile "com.github.stachu540.Glitch:glitch-core:dev-SNAPSHOT"
}
```

### Release
```groovy
repositories {
    jcenter()
}

dependencies {
    compile "com.github.stachu540:glitch-core:$version"
}
```

## Maven

### Development Build
```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
          <groupId>com.github.stachu540.Glitch</groupId>
          <artifactId>glitch-core</artifactId>
          <version>dev-SNAPSHOT</version>
     </dependency>
</dependencies>
```


### Release
```xml
<repositories>
    <repository>
        <id>jcenter</id>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
          <groupId>com.github.stachu540</groupId>
          <artifactId>glitch-core</artifactId>
          <version>${glitch.version}</version>
     </dependency>
</dependencies>
```

# TODO

* [ ] [Chat](../chat)
* [ ] [Kraken](../kraken)
* [ ] [Helix](../helix)
* [ ] [PubSub](../pubsub)
