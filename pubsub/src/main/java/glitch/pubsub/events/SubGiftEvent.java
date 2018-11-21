package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.GiftSubscriptionMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubGiftEvent extends AbstractMessageEvent<GiftSubscriptionMessage> implements IEvent<GlitchPubSub> {
    public SubGiftEvent(GlitchPubSub client, Topic topic, GiftSubscriptionMessage data) {
        super(client, topic, data);
    }
}
