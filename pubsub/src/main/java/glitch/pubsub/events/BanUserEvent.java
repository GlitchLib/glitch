package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.Ban;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BanUserEvent extends AbstractMessageEvent<Ban> implements IEvent<GlitchPubSub> {
    public BanUserEvent(GlitchPubSub client, Topic topic, Ban data) {
        super(client, topic, data);
    }
}
