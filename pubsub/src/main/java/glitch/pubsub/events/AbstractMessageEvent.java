package glitch.pubsub.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractMessageEvent<T> extends AbstractEvent<GlitchPubSub> implements IEvent<GlitchPubSub> {
    private final Topic topic;
    private final T data;

    protected AbstractMessageEvent(GlitchPubSub client, Topic topic, T data) {
        super(client);
        this.topic = topic;
        this.data = data;
    }
}
