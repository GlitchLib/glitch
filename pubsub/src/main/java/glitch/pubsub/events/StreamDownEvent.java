package glitch.pubsub.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class StreamDownEvent extends AbstractEvent<GlitchPubSub> implements IEvent<GlitchPubSub> {
    private final Topic topic;
    private final Instant serverTime;
    public StreamDownEvent(GlitchPubSub client, Topic topic, Instant serverTime) {
        super(client);
        this.topic = topic;
        this.serverTime = serverTime;
    }
}
