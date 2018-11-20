package glitch.chat.events;

import glitch.api.ws.events.Event;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.ChannelEntity;
import glitch.chat.object.entities.ChannelUserEntity;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartUserChannelEvent extends Event<GlitchChat> {
    private final Mono<ChannelUserEntity> user;
    private final Mono<ChannelEntity> channel;

    public PartUserChannelEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.user = this.channel.flatMap(entity -> entity.getUser(message.getPrefix().getNick()));
    }
}
