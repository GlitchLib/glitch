package glitch.pubsub.events;

import glitch.api.ws.events.IEvent;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.object.json.MessageDelete;

public class MessageDeleteEvent extends AbstractMessageEvent<MessageDelete> implements IEvent<GlitchPubSub> {
    public MessageDeleteEvent(GlitchPubSub client, Topic topic, MessageDelete data) {
        super(client, topic, data);
    }
}
