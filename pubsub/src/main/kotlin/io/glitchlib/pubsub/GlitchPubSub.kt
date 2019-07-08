package io.glitchlib.pubsub

import io.glitchlib.GlitchClient
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.ws.GlitchSocketObject
import io.glitchlib.model.OpenEvent
import io.glitchlib.model.PingEvent
import io.reactivex.Flowable

class GlitchPubSub(
        client: GlitchClient
) : GlitchSocketObject(
        client as GlitchClientImpl,
        "ws${if (client.settings.isConnectionSecure) "s" else ""}://irc-ws.chat.twitch.tv",
        PubSubFormatter(client)
) {

    init {
        onEvent<OpenEvent>().subscribe {
            activateAll()
        }
        onEvent<PingEvent>().subscribe {
            sendRaw(Flowable.just("{}")).subscribe()
        }
    }

    fun register(topic: Topic) =
            (client as GlitchClientImpl).let {
                if (!it.settings.topics.contains(topic)) {
                    it.settings.topics.add(topic)
                }
                return@let activate(topic)
            }

    fun unregister(topic: Topic) =
            (client as GlitchClientImpl).let {
                if (it.settings.topics.contains(topic)) {
                    it.settings.topics.remove(topic)
                }
                return@let disable(topic)
            }

    internal fun activate(topic: Topic) =
            sendRaw(Flowable.just((client as GlitchClientImpl).http.gson.toJson(topic.listen())))

    internal fun disable(topic: Topic) =
            sendRaw(Flowable.just((client as GlitchClientImpl).http.gson.toJson(topic.unlisten())))

    internal fun activateAll() =
            client.settings.topics.map { (client as GlitchClientImpl).http.gson.toJson(it.listen()) }.let {
                sendRaw(Flowable.fromIterable(it))
            }

    internal fun disableall() =
            client.settings.topics.map { (client as GlitchClientImpl).http.gson.toJson(it.unlisten()) }.let {
                sendRaw(Flowable.fromIterable(it))
            }
}