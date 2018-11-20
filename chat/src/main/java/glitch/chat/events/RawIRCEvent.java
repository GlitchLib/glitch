package glitch.chat.events;

import glitch.api.ws.events.Event;
import glitch.chat.GlitchChat;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class RawIrcEvent extends Event<GlitchChat> {
    private final Message message;

    public RawIrcEvent(GlitchChat client, Message message) {
        super(client);
        this.message = message;
    }

    @Override
    public Instant getCreatedAt() {
        return message.getTags().getSentTimestamp();
    }
}
