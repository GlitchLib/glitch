---
title: Netbeans - IDE Setup - Glitch
---

## About

**NetBeans** is an open-source **Integrated Development Environment** (IDE) for developing with **Java**, **PHP**, **C++**, and *other programming languages*.
**NetBeans** is also referred to as a platform of modular components used for developing *Java desktop applications*.

## Setup
[Eclipse](https://www.eclipse.org) is another choice for advancements programming and old known IDE before IntelliJ.
Before starting configuration this IDE we need [download IDE](https://www.eclipse.org/downloads/) and some plugins.

### Install additional plugins.
	
For this step we needed make sure if we have booth extensions required for working of this project.
- [Gradle Support](http://plugins.netbeans.org/plugin/44510/gradle-support)
- [Kotlin](http://plugins.netbeans.org/plugin/68590/kotlin)

You can use in menu **Tools** > **Plugins**. In **Available Plugins** tab type `Gradle Support` or/and `Kotlin` in search field. Mark them and click **Install**.
Shows Installer window, accept a licensing conditions to continue installations.
	
### Import project using link for git project.

- Go to **Team** > **Git** > **Clone**
- Paste your link into **Repository URL** field and press **Next >**. Of course before that you can specified Destination Folder for cloning our project.
- Choose branches for listening. If you want it. Very important!!! You need listen a `dev` branch because we are pushing there before pushing into `master` branch and create Release.
- In next step select **Checkout Branch** to `dev`, and click **Finish**
- After cloned project will show information alert when asking us when we want open this project. We will do pressing **Open Project**

Now IDE will automatically finds a Gradle project and will execute tasks for preparing project to start coding. Everything is on the Gradle Plugin.