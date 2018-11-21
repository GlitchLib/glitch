package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.StreamUp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StreamUpEvent extends AbstractMessageEvent<StreamUp> implements IEvent<GlitchPubSub> {
    public StreamUpEvent(GlitchPubSub client, Topic topic, StreamUp data) {
        super(client, topic, data);
    }
}
