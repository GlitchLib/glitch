package glitch.chat.cache;

import glitch.chat.GlitchChat;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ChatCache {
    private final Set<Channel> channelCache = new LinkedHashSet<>();
    private final Set<User> userCache = new LinkedHashSet<>();

    private final GlitchChat chat;

    public ChatCache(GlitchChat chat) {
        this.chat = chat;

        registerListeners();
    }

    public Optional<Channel> findChannel() {
        return Optional.empty();
    }

    public Optional<User> findUser() {
        return Optional.empty();
    }

    private void registerListeners() {
//        chat.listenOn(ChannelMessageEvent.class).subscribe(event -> {
//
//        });
    }
}