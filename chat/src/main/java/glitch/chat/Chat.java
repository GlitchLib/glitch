package glitch.chat;

import glitch.chat.MessageInterface;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Chat {
    @Getter(AccessLevel.NONE)
    private final MessageInterface tmi;
    private final Channel channel;
    private final Set<Chat.User> chatUsers = new LinkedHashSet<Chat.User>();

    public static class User {
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
