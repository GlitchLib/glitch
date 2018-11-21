package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.Following;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FollowEvent extends AbstractMessageEvent<Following> implements IEvent<GlitchPubSub> {
    public FollowEvent(GlitchPubSub client, Topic topic, Following message) {
        super(client, topic, message);
    }
}
