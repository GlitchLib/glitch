package glitch.chat.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import glitch.api.objects.json.Badge;
import glitch.api.ws.events.AbstractEvent;
import glitch.api.ws.events.IEvent;
import glitch.chat.GlitchChat;
import glitch.chat.object.irc.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelUserStateEvent extends AbstractEvent<GlitchChat> implements IEvent<GlitchChat> {
    private final ImmutableSet<Badge> badges;
    private final Color color;
    private final String displayName;
    private final ImmutableList<Integer> emoteSets;
    private final boolean mod;

    public ChannelUserStateEvent(GlitchChat client, Message message) {
        super(client);
        this.badges = message.getTags().getBadges();
        this.color = message.getTags().getColor();
        this.displayName = message.getTags().get("display-name");
        this.emoteSets = message.getTags().getEmoteSets();
        this.mod = message.getTags().getBoolean("mod");
    }
}
