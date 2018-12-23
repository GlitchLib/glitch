package glitch.chat.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import glitch.api.objects.json.Badge;
import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.events.interfaces.IMessageEvent;
import glitch.chat.object.entities.UserEntity;
import glitch.chat.object.irc.Emote;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

import java.awt.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateMessageEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final ImmutableSet<Badge> badges;
    private final Color color;
    private final String displayName;
    private final long userId;
    private final String message;
    private final ImmutableList<Emote> emotes;

    private final Mono<UserEntity> user;

    public PrivateMessageEvent(GlitchChat client, Message message) {
        super(client);
        this.message = message.getFormattedTrailing();
        this.emotes = message.getTags().getEmotes();
        this.badges = message.getTags().getBadges();
        this.color = message.getTags().getColor();
        this.displayName = message.getTags().get("display-name");
        this.userId = message.getTags().getLong("user-id");

        this.user = client.getUser(message.getPrefix().getNick());
    }

    public boolean isTurbo() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("turbo"));
    }

    public boolean isPrime() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("premium"));
    }

    public boolean isAdmin() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("admin"));
    }

    public boolean isStaff() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("staff"));
    }

    @Deprecated
    public boolean isGlobalMod() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("global_mod"));
    }
}
