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
    public abstract List<Badge> getBadges();

    public abstract Color getColor();

    public abstract boolean hasMod();

    public abstract boolean isSubscriber();

    public abstract boolean hasTurbo();

    public abstract String getMessage();
}
