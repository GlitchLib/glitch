!!! caution
    Some part of the events is not done yet. This documentations shows only example, they might be changed in time.
    
!!! todo
    This page needs improvements. We will do this as soon as possible.

# Getting Started

Before start we need initialize [`client`](../client) first at all. After initialization we can now getting into the [next step](#initialize-message-intereface-api)

## Initialize Message Intereface API

```java
GlitchChat chat = GlitchChat.builder(client)
        .botCredentials("<access_token>", "<refresh_token>")
        .build(); // or .buildAsync() which returns Mono<GlitchChat>

// connecting
chat.connect().block(); /* returns void - block() is required to execute connections */
```

## What's next?

The next step is create communications between channel and bot enviornment. This examples below will you meet them every time.

### Join / Leave channel

This example presenting how to join channel, when you have initialized bot from builder (**see: [how to initialize message interface](#initialize-message-intereface-api)**)

> For e.g. We wana join to `twitch` channel.

```java
chat.join("twitch");
```

Another sample is leaving bot from channel

> For e.g. We wana leaving already joined `twitch` channel.

```java
chat.getChannel("twitch").leave().subscribe(); /* subscribe)_ is necessarily to initialize leaving channel */
```

### Get Chat message

```java
chat.on(MessageEvent.class).subscribe(event -> {
  if (event.getMessage().equalsIgnoreCase("!ping")) {
    event.sendMessage(Mono.just(String.format("%s, Pong!", event.getUser().getMentionable())));
  }
});
```

### Listen all moderation actions

```java
chat.on(ModerationEvent.class).subscribe(event -> {
  if (event.getAction() == ModerationAction.BAN) {
    event.sendMessage(Mono.just(String.format("%s has been banned from this channel!", event.getUser().getMentionable())));
  }
});
```

## Using Commands API

Soon &trade;
