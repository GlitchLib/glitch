---
title: Maven - Build Setup - Glitch
---

## Repository

```xml fct_label="Maven"
<repositories>
  <repository>
    <id>jcenter</id>
    <url>https://jecenter.bintray.com/</url>
  </repository>
</repositories>
```
```xml fct_label="Maven (Optiona repository)"
<repositories>
  <repository>
    <id>GlitchLib</id>
    <url>https://dl.bintray.com/stachu540/GlitchLib</url>
  </repository>
</repositories>
```

## With Dependency Managing `BOM` (Bills of Materials)

```xml fct_label="Maven"
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
```

## With all or specific required component

```xml fct_label="Maven 'all'"
<dependencies>
  <dependency>
    <groupId>io.glitchlib</groupId>
    <artifactId>glitch-all</artifactId>
    <version>${glitch.version}</version>
  </dependency>
</dependencies>
```

```xml fct_label="Maven 'all'"
<dependencies>
  <dependency>
    <!-- this dependency is required for import other dependencies -->
    <groupId>io.glitchlib</groupId>
    <artifactId>glitch-core</artifactId>
    <version>${glitch.version}</version>
  </dependency>
  <dependency>
    <groupId>io.glitchlib</groupId>
    <artifactId>glitch-kraken</artifactId>
    <version>${glitch.version}</version>
  </dependency>
</dependencies>
```