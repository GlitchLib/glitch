package glitch.chat.events.chat;

import glitch.chat.events.channel.ChannelUserEvent;
import glitch.core.api.json.Badge;
import glitch.socket.utils.EventImmutable;
import java.awt.Color;
import java.util.List;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RitualNoticeEvent extends ChannelUserEvent {
    List<Badge> getBadges();

    Color getColor();

    boolean hasMod();

    boolean isSubscriber();

    boolean hasTurbo();

    String getMessage();
}
