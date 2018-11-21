package glitch.chat.object.json;

import lombok.Data;

import java.util.Locale;

@Data
public class RoomState {
    private Locale broadcasterLanguage;
    private boolean emoteOnly;
    private Long follow;
    private boolean r9k;
    private Long slow;
    private boolean subsOnly;

    public boolean isFollowersOnly() {
        return follow != -1L;
    }

    public boolean isSlowMode() {
        return slow > 0;
    }
}
