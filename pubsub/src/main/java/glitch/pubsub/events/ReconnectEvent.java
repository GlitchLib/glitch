package glitch.pubsub.events;

import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface ReconnectEvent extends Event<GlitchPubSub> {
}
