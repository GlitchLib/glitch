package io.glitchlib.tmi

import io.glitchlib.GlitchClient
import io.glitchlib.GlitchException
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.ws.GlitchSocketObject
import io.glitchlib.model.PingEvent
import io.glitchlib.tmi.event.Events
import io.glitchlib.tmi.event.IRCEvent
import io.glitchlib.tmi.irc.IrcFormatter
import io.reactivex.Flowable

class MessageInterface(client: GlitchClient) : GlitchSocketObject(
        client as GlitchClientImpl,
        "ws${if (client.settings.isConnectionSecure) "s" else ""}://irc-ws.chat.twitch.tv",
        IrcFormatter(client)
) {
    init {
        if (client.settings.botUser == null) {
            throw GlitchException("Cannot initialize message interface without credentials for bot!")
        }
        // Converting to current supported events
        onEvent<IRCEvent>().subscribe {
            Events.from(it, eventSubject::onNext)
        }
        // Keeps Connection Alive
        onEvent<PingEvent>().subscribe {
            sendRaw(Flowable.just("PONG :tmi.twitch.tv"))
        }
    }
}