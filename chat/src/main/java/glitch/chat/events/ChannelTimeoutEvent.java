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
public class ChannelTimeoutEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Mono<ChannelUserEntity> user;
    private final Mono<ChannelEntity> channel;
    private final Optional<String> reason;
    private final int duration;

    public ChannelTimeoutEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.user = this.channel.flatMap(c -> c.getUser(message.getTrailing()));
        this.duration = message.getTags().getInteger("ban-duration");
        this.reason = Optional.ofNullable(message.getTags().getOrDefault("ban-reason", null));
    }
}
