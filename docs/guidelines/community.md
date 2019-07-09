---
title: Community Guidelines - Glitch
---

# How to configure your IDE

Configuring IDE is important to starting contribute. We do like preferred use [IntelliJ IDEA](https://www.jetbrains.com/idea/).
Considering other IDE is possible if:

* Have annotation processor support like plugins, extension etc.
* Supporting Gradle project
* Having installed Git (optional supported as plugin or extension)
* Supporting Java Development Kit 8 and late

## Prepare your IDE
Before start we need a [Java Development Kit 8 (JDK8)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Install it and configure environmental variables: `JAVA_HOME` if not exist, create and adding to `PATH` - `$JAVA_HOME/bin`.
After this step you can choose one of 3 popular IDE's below:

* [IntelliJ](../ide-setup/intellij)
* [Eclipse](../ide-setup/eclipse)
* [Netbeans](../ide-setup/netbeans)

# How to contribute to Glitch

Of course you can contribute to our repository in 3 ways:

* [Creating Issue](#issue)
* [Joining to our Discord Server](#discord-server)
* [Create Pull Request](#pull-requests)

## Issue

All your ideas, features, bug reports, etc. must be documented in following comments on [issue](https://github.com/glitchlib/glitch/issues/new) tab.
Questions in issue are not welcome. Use Discord server below to asking developers for your question.

## Discord Server

Your problem is a hardest to reproduce? Having a questions, how to and where? Come, join to our [Discord Server](https://discord.gg/nJJ2fDM)

## Pull Requests

So, you are thinking about sending a pull request? Awesome! But... before starting on your pull request, you should read up on
the [pull request](https://github.com/glitchlib/glitch/compare) comment.

Before starting pull request make sure if:

1. All your code will be subject to the project's licence, in this case [MIT](https://github.com/glitchlib/glitch/blob/master/LICENSE).
2. Your code follows a style requirements mentioned below, and you didn't modified project style (ex. indentation style, bracket style, naming, comments, etc). Rewrites of certain systems are kindly welcome too.
3. _Your pull-request must be created **ONLY** against the `dev` branch!_

#### Style Requirements

* All POJO classes must be a Kotlin `data class`
* All object instances, methods and fields must be documented for getting sure what does things are doing.
* All implementation subjects are be optional to documenting. Any classes with `Impl` suffix are be ignorable from compiler.
* Documented notes (backslashes `//` or slashed stars `/* */`) is optional into inside methods for getting sure what does things are doing.
* If some parts of code is unofficial and they are using non-documented part of [Twitch API](https://dev.twitch.tv/docs), you must mark them using `@Unofficial("<link to source>")` annotation adding inside the source.
* Documenting object instances (classes, interfaces, enums, variables, functions) must have a same format, if they are accessible like `public` or `protected` (examples below).
More about documenting Kotlin Code we will send you to [this page](https://kotlinlang.org/docs/reference/kotlin-doc.html):

```kotlin fct_label="Class/Interface"
/**
 * {Your short description}
 * 
 * {Your longest description}
 * @author [{Full name or Username}]({Github link or e-mail address})
 * @since {next minor version (eg. 0.11.0)}
 */
interface Example
```

```kotlin fct_label="Functions"
/**
 * {Your short description}
 * 
 * {Your longest description}
 * @property field {description about variable field}
 * @return {description about returned variable}
 */
fun function(field: String) : Any = field
```

```kotlin fct_label="Variables"
/**
 * {Your short description}
 * 
 * {Your longest description}
 * @get {description returned variable}
 * @set {description setted variable}
 */
var exampleVariable: Long = 330498495213872469L
```
