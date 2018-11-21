package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.ModeratorActivation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubscribersOnlyEvent extends AbstractMessageEvent<ModeratorActivation> implements IEvent<GlitchPubSub> {
    public SubscribersOnlyEvent(GlitchPubSub client, Topic topic, ModeratorActivation data) {
        super(client, topic, data);
    }
}
