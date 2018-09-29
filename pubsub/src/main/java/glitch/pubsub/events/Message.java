package glitch.pubsub.events;

import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Message extends Event<GlitchPubSub> {
    Type getType();

    enum Type {
        PING,
        PONG,
        RECONNECT,
        LISTEN,
        UNLISTEN,
        RESPONSE,
        MESSAGE
    }
}
