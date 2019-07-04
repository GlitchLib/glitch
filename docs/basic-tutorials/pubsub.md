!!! caution
    Some part of the events is not done yet. This documentations shows only example, they might be changed in time.
    
!!! todo
    This page needs improvements. We will do this as soon as possible.
    
!!! caution
    Currently **Extension Broadcast System** is not supported on this library. In the next releases will be supporting as well.
    
# Getting Started

Before start we need initialize [`client`](../client) first at all. After initialization we can now getting into the [next step](#initialize-message-intereface-api)

## Initialization Client

```java
GlitchPubSub client = GlitchPubSub.builder(client)
              // register topic after initialize connection
              .activateTopic(Topic.videoPlayback("<channel_name>"))
              // register topic and don't activate it
              .setTopic(Topic.following(<channel_id>))
              .build(); // or .buildAsync() which returns Mono<GlitchPubSub>

// connecting
chat.connect().block(); /* returns void - block() is required to execute connections */
```

## Listening who's following a channel

```java
client.listenOn(FollowEvent.class).subscribe(event -> {
  System.out.println(String.format("%s is following the channel!", event.getData().getUsername()));
});
```

## Getting streams in live

First we need initialize required topic which is `Topic.videoPlayback​("<user_name>")` (not Display Name)


``` java fct_label="Stream starting"
client.listenOn(StreamUpEvent.class).subscribe(event -> {
  System.out.println(String.format("[%s] Channel %s is starting stream with delay: %d!",
          event.getData().getServerTime().toString(),
          event.getData().getTopic().getSuffix()[0],
          event.getData().getDelay()));
});
```

``` java fct_label="Viewer Count"
client.listenOn(ViewCountEvent.class).subscribe(event -> {
  System.out.println(String.format("[%s] Channel %s viwers: %d!",
          event.getData().getServerTime().toString(),
          event.getData().getTopic().getSuffix()[0],
          event.getData().getViewers()));
});
```

``` java fct_label="Stream stopping"
client.listenOn(StreamDownEvent.class).subscribe(event -> {
  System.out.println(String.format("[%s] Channel %s has been stopped!",
          event.getData().getServerTime().toString(),
          event.getData().getTopic().getSuffix()[0]));
});
```