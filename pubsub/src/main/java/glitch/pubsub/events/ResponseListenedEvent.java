package glitch.pubsub.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.exceptions.PubSubException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseListenedEvent extends AbstractEvent<GlitchPubSub> implements IEvent<GlitchPubSub> {
    private final UUID nonce;
    private final PubSubException error;

    public ResponseListenedEvent(GlitchPubSub client, UUID nonce, PubSubException error) {
        super(client);
        this.nonce = nonce;
        this.error = error;
    }
}
