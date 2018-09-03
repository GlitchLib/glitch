package glitch.chat;

import com.google.common.collect.SetMultimap;
import com.google.common.collect.MultimapBuilder;

public class ChannelCache {
    private final SetMultimap<Channel, Chat> chats = MultimapBuilder.linkedHashKeys().hashSetValues().build();

    public Chat getChatRoom(String channel, String roomId) {

    }
}
