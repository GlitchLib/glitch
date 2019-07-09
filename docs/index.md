---
title: Overview - Glitch
---
!!! warning
    Currently examples are no longer worked cause whole code has been changed. We will improve this documentation **ASAP**

# Why Glitch?

* **Reactive Streams**

    With [**Project Reactor**](https://projectreactor.io), reactive streams is soo easy to use. Without blocking `IO` we can build a awesome tools using `JVM` environment.

* **Faster than light**

    With reactive streams, the response time minimalize risk throwing any `Exception`. You choose which response can will be.
    
* **Android**
    
    `API` and `CredentialManager` can be supportive for Android's.
    You choose what you querying request to the `API` and which user you need manage to your application.
    
* **Kotlin**
    
    Supports **Kotlin** we will provide many options for your code.
    We can simplify your work, provides supports for this programming language which it is **Kotlin**.

## Here are some examples!

#### Listen the message

A simple template to start, logging and `!ping` with anwsering `Pong!`?It is possible to do with this example below.

```java
public class MyExampleBot {
  public static void main(String[] args) {
    Property properties = new Property();
    try {
      properties.load("glitch.properties");
    } catch (IOException ignore) {}

    GlitchChat client = GlitchClient.builder()
          .clientId(properties.getProperty("twitch.client_id"))
          .clientSecret(properties.getProperty("twitch.client_secret"))
         .buildAsync().flatMap(client -> GlitchChat.builder(client)
                .botCredentials(properties.getProperty("twitch.bot.access_token"), properties.getProperty("twitch.bot.refresh_token"))
                .buildAsync()).block();

    client.listenOn(MessageEvent.class).subscribe(event -> {
      if (event.getMessage().equalsIgnoreCase("!ping")) {
        event.getChannel().send("Pong!");
      }
    });
  }
}
```

```kotlin
fun main(args: Array<String>) {
    val properties = Property().apply {
        load("glitch.properties")
    }

    val client = GlitchClient {
        clientId(properties.getProperty("twitch.client_id"))
        clientSecret(properties.getProperty("twitch.client_secret"))
        botCredentials(properties.getProperty("twitch.bot.access_token"), properties.getProperty("twitch.bot.refresh_token"))
    }
    val tmi = client.tmi

    
    tmi.listenOn<MessageEvent>().subscribe {
      if (it.message.equals("!ping", ignoreCase = true)) {
        it.channel.send("Pong!");
      }
    }

}
```
#### Give him some power for ban hammer users

For your granting wish we provide a ban hammer power with a including methods inside the `MessageEvent`.

```java
tmi.listenOn(MessageEvent.class).subscribe(event -> {
  if (event.getMessage().match("([nN(|\|)][iI1(|)][gG]+[eEaA43][rR]*?)")) {
    event.getUser().subscribe(user -> user.ban(String.format("Saying: %s", event.getMessage()).subscribe()));
  }
});
```
```kotlin
tmi.listenOn<MessageEvent>().subscribe {
  if (it.getMessage().match("([nN(|\|)][iI1(|)][gG]+[eEaA43][rR]*?)")) {
    it.user.subscribe { user.ban(String.format("Saying: %s", event.getMessage()).subscribe()) }
  }
}
```
