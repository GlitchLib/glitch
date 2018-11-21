package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.BitsMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BitsEvent extends AbstractMessageEvent<BitsMessage> implements IEvent<GlitchPubSub> {
    public BitsEvent(GlitchPubSub client, Topic topic, BitsMessage data) {
        super(client, topic, data);
    }
}
