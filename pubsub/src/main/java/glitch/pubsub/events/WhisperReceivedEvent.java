package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.WhisperMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WhisperReceivedEvent extends AbstractMessageEvent<WhisperMessage> implements IEvent<GlitchPubSub> {
    public WhisperReceivedEvent(GlitchPubSub client, Topic topic, WhisperMessage data) {
        super(client, topic, data);
    }
}
