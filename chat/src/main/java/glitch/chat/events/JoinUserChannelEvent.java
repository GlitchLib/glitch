package glitch.chat.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.ChannelEntity;
import glitch.chat.object.entities.ChannelUserEntity;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = true)
public class JoinUserChannelEvent extends AbstractEvent<GlitchChat> {
    private final Mono<ChannelUserEntity> user;
    private final Mono<ChannelEntity> channel;

    public JoinUserChannelEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.user = this.channel.zipWith(Mono.just(message.getPrefix().getNick()))
                .flatMap(tuple -> tuple.getT1().getUser(tuple.getT2()).defaultIfEmpty(new ChannelUserEntity(tuple.getT1(), tuple.getT2(), Collections.emptySet())));
    }
}
