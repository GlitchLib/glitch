package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.CommerceMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommerceEvent extends AbstractMessageEvent<CommerceMessage> implements IEvent<GlitchPubSub> {
    public CommerceEvent(GlitchPubSub client, Topic topic, CommerceMessage data) {
        super(client, topic, data);
    }
}
