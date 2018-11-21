package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.ViewCount;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ViewCountEvent extends AbstractMessageEvent<ViewCount> implements IEvent<GlitchPubSub> {
    public ViewCountEvent(GlitchPubSub client, Topic topic, ViewCount data) {
        super(client, topic, data);
    }
}
