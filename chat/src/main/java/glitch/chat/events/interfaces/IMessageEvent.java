package glitch.chat.events.interfaces;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import glitch.api.objects.json.Badge;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.entities.IEntity;
import glitch.chat.object.irc.Emote;
import java.awt.Color;
import javax.annotation.Nullable;
import reactor.core.publisher.Mono;

public interface IMessageEvent<T extends IEntity> extends IEvent<GlitchChat> {
    Color getColor();
    ImmutableSet<Badge> getBadges();
    Mono<T> getUser();
    String getContent();
    @Nullable
    ImmutableList<Emote> getEmotes();
    Mono<? extends IEntity> getSender();

    default Mono<Void> response(String message) {
        return response(message, false);
    }

    @SuppressWarnings("unchecked")
    default Mono<Void> response(String message, boolean mentionable) {
        return getSender().flatMap(e -> e.response(message, mentionable));
    }
}
