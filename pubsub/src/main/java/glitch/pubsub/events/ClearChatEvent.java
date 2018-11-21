package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.Moderator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClearChatEvent extends AbstractMessageEvent<Moderator> implements IEvent<GlitchPubSub> {
    public ClearChatEvent(GlitchPubSub client, Topic topic, Moderator data) {
        super(client, topic, data);
    }
}
