package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.Unban;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnbanUserEvent extends AbstractMessageEvent<Unban> implements IEvent<GlitchPubSub> {
    public UnbanUserEvent(GlitchPubSub client, Topic topic, Unban data) {
        super(client, topic, data);
    }
}
