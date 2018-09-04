package glitch.chat;

import glitch.kraken.json.Channel;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Chat {
    @Getter(AccessLevel.NONE)
    private final MessageInterface tmi;
    private final Channel channel;
    private final Set<User> chatUsers = new LinkedHashSet<>();

    public static interface User {
        long getId();
        String getUsername();
        String getDisplayName();
        boolean isModerator();
        boolean isSubscriber();
        boolean isGlobalMod();
        boolean isStaff();
        boolean isAdmin();
        boolean hasTurbo();
        boolean hasPrime();

        void sendPrivateMessage(String message);
    }
}
