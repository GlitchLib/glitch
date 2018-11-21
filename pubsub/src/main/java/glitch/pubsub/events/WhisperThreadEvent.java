package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.WhisperThread;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WhisperThreadEvent extends AbstractMessageEvent<WhisperThread> implements IEvent<GlitchPubSub> {
    public WhisperThreadEvent(GlitchPubSub client, Topic topic, WhisperThread data) {
        super(client, topic, data);
    }
}
