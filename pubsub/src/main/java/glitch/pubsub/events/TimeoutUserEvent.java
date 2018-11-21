package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.Timeout;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimeoutUserEvent extends AbstractMessageEvent<Timeout> implements IEvent<GlitchPubSub> {
    public TimeoutUserEvent(GlitchPubSub client, Topic topic, Timeout data) {
        super(client, topic, data);
    }
}
