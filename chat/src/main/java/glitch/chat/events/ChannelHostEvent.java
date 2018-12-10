package glitch.chat.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.ChannelEntity;
import glitch.chat.object.entities.UserEntity;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelHostEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Mono<ChannelEntity> channel;
    private final Mono<UserEntity> hostedChannel;
    private final int numberOfViews;

    public ChannelHostEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.hostedChannel = client.getUser(message.getMiddle().get(1));
        this.numberOfViews = Integer.parseInt(message.getMiddle().get(2));
    }
}
