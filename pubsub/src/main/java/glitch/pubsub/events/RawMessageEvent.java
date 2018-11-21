package glitch.pubsub.events;

import com.google.gson.JsonElement;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RawMessageEvent extends AbstractMessageEvent<JsonElement> implements IEvent<GlitchPubSub> {
    public RawMessageEvent(GlitchPubSub client, Topic topic, JsonElement jsonElement) {
        super(client, topic, jsonElement);
    }
}
