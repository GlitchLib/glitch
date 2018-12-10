package glitch.chat.events;

import com.google.common.collect.ImmutableSet;
import glitch.api.objects.json.Badge;
import glitch.api.objects.json.interfaces.IDObject;
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
public class ChannelMessageEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat>, IDObject<String> {
    private final Mono<ChannelEntity> channel;
    private final Mono<ChannelUserEntity> user;
    private final String content;
    private final ImmutableSet<Badge> badges;
    private final boolean actionMessage;
    private final String id;

    public ChannelMessageEvent(GlitchChat client, Message message) {
        super(client, message.getTags().getSentTimestamp());
        this.content = message.getFormattedTrailing();
        this.channel = client.getChannel(message.getMiddle().get(0).substring(1));
        this.badges = message.getTags().getBadges();
        this.user = this.channel.map(c -> new ChannelUserEntity(c, message.getPrefix().getNick(), badges));
        this.actionMessage = message.isActionMessage();
        this.id = message.getTags().get("id");
    }

    public Mono<Void> deleteMessage() {
        return this.channel.flatMap(e -> e.moderate().delete(this.id));
    }
}
