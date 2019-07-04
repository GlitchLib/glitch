---
title: Eclipse - IDE Setup - Glitch
---

## About

**Eclipse** is an open-source platform of extensible software development application frameworks, tools and run times that was initially created as a _Java-based **Integrated Development Environment**_ (IDE). 

Eclipse's runtime system is based on a collection of **Equinox Open Services Gateway Initiative** (OSGi) runtime-built open-source projects covering **Java IDE**, **static/dynamic languages**, **thick/thin-client** and **server-side frameworks**, **modeling/business reporting** and **embedded/mobile systems**.

## Setup
[Eclipse](https://www.eclipse.org) is another choice for advancements programming and old known IDE before IntelliJ.
Before starting configuration this IDE we need [download IDE](https://www.eclipse.org/downloads/) and some plugins.

### Install additional plugins.
	
For this step we needed make sure if we have those extensions required for working of this project.
- [EGit - Git Integration for Eclipse](https://marketplace.eclipse.org/content/egit-git-integration-eclipse)
- [Buildship Gradle Integration](https://marketplace.eclipse.org/content/buildship-gradle-integration)
- [Kotlin Plugin](https://marketplace.eclipse.org/content/kotlin-plugin-eclipse)
	
### Import project using link for git project.
If IDE have it implemented in installation we can proceed to the steps below:
	
- Go to **File** > **Import**  and  select > **Git** > **Projects from Git**.
- Press **Next >** and select **Clone URI** with pressing **Next >**.
- Paste your link in **URI** field and press **Next >**.
- Select specific branches what you need to work with them and press **Next >**.
- To start cloning repository specify **Directory**, **Initial branch** (recommending select `dev` branch) and **Remote name**. I think you need change only directory because of rest is find and we will leave this.
- Final step is select **Import as general project**. If we trying use **Import using the New Project wizard**, Gradle extension will not allow us to import, only creation.
- That's all of them. Import as new **general** project and press **Finish** to start.
- On the *right side* ide you will find a distinctive a Gradle elephant logo. Click him to expand **Gradle Task View** tab. You will find this message `There are no Gradl;e projects in the current workspace. Import a Gradle project to see its tasks in the Gradle Task View`. Click to **Import a Gradle Project**
- Specify a **Project root directory** if this field is empty. Sometimes we need specify a Working sets. Create them one using **New** button, and choose **Resource**. Before finishing and selecting our project we must naming a **Working set name**. Than click **Finish**.
	
