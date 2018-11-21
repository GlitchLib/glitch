package glitch.pubsub.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReconnectAlertEvent extends AbstractEvent<GlitchPubSub> implements IEvent<GlitchPubSub> {
    public ReconnectAlertEvent(GlitchPubSub client) {
        super(client);
    }
}
