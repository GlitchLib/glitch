package glitch.chat.events;

import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelStateEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final Locale broadcasterLanguage;
    private final boolean emoteOnly;
    private final Long follow;
    private final boolean r9k;
    private final Long slow;
    private final boolean subsOnly;

    public ChannelStateEvent(GlitchChat client, Message message) {
        super(client);
        this.broadcasterLanguage = message.getTags().getBroadcasterLanguage();
        this.emoteOnly = message.getTags().getBoolean("emote-only");
        this.follow = message.getTags().getLong("followers-only");
        this.r9k = message.getTags().getBoolean("r9k");
        this.slow = message.getTags().getLong("slow");
        this.subsOnly = message.getTags().getBoolean("subs-only");
    }

    public boolean isFollowersOnly() {
        return follow != -1L;
    }

    public boolean isSlowMode() {
        return slow > 0;
    }
}
