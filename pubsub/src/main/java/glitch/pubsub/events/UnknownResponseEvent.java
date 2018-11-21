package glitch.pubsub.events;

import com.google.gson.JsonElement;
import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnknownResponseEvent extends AbstractEvent<GlitchPubSub> implements IEvent<GlitchPubSub> {
    private final JsonElement data;
    public UnknownResponseEvent(GlitchPubSub client, JsonElement data) {
        super(client);
        this.data = data;
    }
}
