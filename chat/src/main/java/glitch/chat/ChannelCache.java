package glitch.chat;

import com.google.common.collect.SetMultimap;
import com.google.common.collect.MultimapBuilder;
import glitch.kraken.json.Channel;

public class ChannelCache {
    private final SetMultimap<Channel, Chat> chats = MultimapBuilder.linkedHashKeys().hashSetValues().build();

    public Chat getChatRoom(String channel, String roomId) {
        return null;
    }
}
