package glitch.chat.events.channel;

import glitch.chat.events.chat.RitualNoticeEvent;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RaidEvent extends RitualNoticeEvent {
    long getViewcount();
}
