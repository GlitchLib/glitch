package glitch.chat.events;

import com.google.common.collect.ImmutableSet;
import glitch.api.objects.json.Badge;
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
public class ChannelMessageEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Mono<ChannelEntity> channel;
    private final Mono<ChannelUserEntity> user;
    private final String content;
    private final ImmutableSet<Badge> badges;

    public ChannelMessageEvent(GlitchChat client, Message message) {
        super(client, message.getTags().getSentTimestamp());
        this.content = message.getTrailing();
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.badges = message.getTags().getBadges();
        this.user = this.channel.map(c -> new ChannelUserEntity(c, message.getPrefix().getNick(), badges));
    }
}
