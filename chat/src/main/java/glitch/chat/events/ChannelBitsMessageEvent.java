package glitch.chat.events;

import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelBitsMessageEvent extends ChannelMessageEvent implements IEvent<GlitchChat> {
    private final int bits;

    public ChannelBitsMessageEvent(GlitchChat client, Message message) {
        super(client, message);
        this.bits = message.getTags().getInteger("bits");
    }
}
