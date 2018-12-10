package glitch.chat.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelStateChangedEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final String key;
    private final String value;

    public ChannelStateChangedEvent(GlitchChat client, Message message) {
        super(client);
        this.key = message.getTags().keySet().stream().findFirst().orElse(null);
        this.value = message.getTags().getOrDefault(this.key, null);
    }
}
