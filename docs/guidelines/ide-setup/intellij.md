---
title: IntelliJ - IDE Setup - Glitch
---

## About
**IntelliJ IDEA** is a special programming environment or **Integrated Development Environment** (IDE) largely meant for **Java**.
This environment is used especially for the development of programs.
It is developed by a company called **JetBrains**, which was formally called **IntelliJ**.
It is available in two editions: the Community Edition which is licensed by Apache 2.0, and a commercial edition known as the Ultimate Edition.
Both of them can be used for creating software which can be sold.
What makes **IntelliJ IDEA** so different from its counterparts is its ease of use, flexibility and its solid design.

## Setup
[Jetbrains](https://www.jetbrains.com/) provides a most popular IDE for JVM referenced programming language.
It is a great choice to starting journey with this IDE. That's why we recommending it for people who's like programming in Java.

Before starting configuration this IDE we need [download IDE](https://www.jetbrains.com/idea/download/)

### Import project using link for git project.

Go to **File** > **New** > **Project from Version Control** > **Git**
Paste your link in **URL** field and press **Clone** to start cloning repository.

### Import Gradle Project

In *bottom-right corner* will shows pop-out which inform the Gradle project has been founded and ready to link. Click **Import Gradle Project**.
If you lost this pop-out you can find them in **Event Log** (default: *bottom-right tab*).

Then will shows a window **Import Module from Gradle**. 

* Uncheck **Create separate module per source set** - this one makes issue in annotation processing.
* Select **Use gradle 'wrapper' task configuration** - cause we provide gradle wrapper for this repository. We not recommend using a different gradle version cause about tasks works with bundled wrapper.
* Set **Gradle JVM** if module couldn't found `JAVA_HOME` variable we should add it manually using three dots (`...`) button.
* Press **OK** and wait till IDE finished importing dependencies