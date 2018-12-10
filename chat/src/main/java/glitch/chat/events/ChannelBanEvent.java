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

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelBanEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Mono<ChannelUserEntity> user;
    private final Mono<ChannelEntity> channel;
    private final Optional<String> reason;

    public ChannelBanEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.user = this.channel.flatMap(c -> c.getUser(message.getTrailing()));
        this.reason = Optional.ofNullable(message.getTags().getOrDefault("ban-reason", null));
    }
}
