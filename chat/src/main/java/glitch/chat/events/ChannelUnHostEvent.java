package glitch.chat.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.ChannelEntity;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelUnHostEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Mono<ChannelEntity> channel;
    private final int numberOfViews;

    public ChannelUnHostEvent(GlitchChat client, Message message) {
        super(client);
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.numberOfViews = Integer.parseInt((message.getTrailing() != null) ? message.getTrailing().substring(2) : "0");
    }
}
