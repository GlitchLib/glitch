package glitch.chat.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.ChannelEntity;
import glitch.chat.object.entities.ChannelUserEntity;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelDeleteMessageEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Mono<ChannelEntity> channel;
    private final Mono<ChannelUserEntity> user;
    private final String message;
    private final String targetId;
    private final boolean actionMessage;

    public ChannelDeleteMessageEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.user = this.channel.flatMap(c -> c.getUser(message.getTags().get("login")));
        this.message = message.getFormattedTrailing();
        this.targetId = message.getTags().get("target-msg-id");
        this.actionMessage = message.isActionMessage();
    }
}
