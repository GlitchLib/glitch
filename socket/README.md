**WARNING:** We recommending use [chat](../chat) or [pubsub](./pubsub) dependency. Do not import it to your project.

# Glitch Chat

This module contains a Event Manager and `GlitchWebSocket` abstract class to handling [glitch-chat](../chat) and [glitch-pubsub](../pubsub)

## TODO:

* [x] Base Socket
  * [ ] Multiple instance for [PubSub](../pubsub)
* [x] Raw Handling Response
* [x] Event Manager - Using [RxJava2](http://reactivex.io/)
  * [ ] Reactive Events
  * [ ] Annotated Events
    * [ ] with custom arguments - using annotated parameters