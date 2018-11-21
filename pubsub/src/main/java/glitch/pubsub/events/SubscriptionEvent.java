package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.SubscriptionMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubscriptionEvent extends AbstractMessageEvent<SubscriptionMessage> implements IEvent<GlitchPubSub> {
    public SubscriptionEvent(GlitchPubSub client, Topic topic, SubscriptionMessage data) {
        super(client, topic, data);
    }
}
