package glitch.chat.events.states;

import glitch.chat.events.channel.ChannelEvent;
import glitch.socket.utils.EventImmutable;
import java.awt.Color;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface UserStateEvent extends ChannelEvent {
    Color getColor();

    boolean hasMod();

    boolean isSubscriber();

    boolean hasTurbo();

    String getDisplayName();

    String getUserType();
}
