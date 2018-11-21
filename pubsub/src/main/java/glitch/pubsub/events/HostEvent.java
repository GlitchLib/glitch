package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.Host;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HostEvent extends AbstractMessageEvent<Host> implements IEvent<GlitchPubSub> {
    public HostEvent(GlitchPubSub client, Topic topic, Host data) {
        super(client, topic, data);
    }
}
