package glitch.chat.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import glitch.api.objects.json.Badge;
import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.UserEntity;
import glitch.chat.object.irc.Message;
import glitch.kraken.services.UserService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

import java.awt.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalUserStateEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final ImmutableSet<Badge> badges;
    private final Color color;
    private final String displayName;
    private final ImmutableList<Integer> emoteSets;
    private final long userId;

    public GlobalUserStateEvent(GlitchChat client, Message message) {
        super(client);
        this.badges = message.getTags().getBadges();
        this.color = message.getTags().getColor();
        this.displayName = message.getTags().get("display-name");
        this.emoteSets = message.getTags().getEmoteSets();
        this.userId = message.getTags().getLong("user-id");
    }

    public Mono<UserEntity> getUser() {
        return (getClient().getApi() != null) ? getClient().getApi().use(UserService.class)
                .flatMap(service -> service.getUserById(userId))
                .flatMap(user -> getClient().getUser(user.getUsername())) : Mono.empty();
    }
}
