package glitch.pubsub.events;

import com.google.gson.JsonArray;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelExtensionBroadcastEvent extends AbstractMessageEvent<JsonArray> implements IEvent<GlitchPubSub> {
    public ChannelExtensionBroadcastEvent(GlitchPubSub client, Topic topic, JsonArray data) {
        super(client, topic, data);
    }
}
