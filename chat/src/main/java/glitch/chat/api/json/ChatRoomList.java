package glitch.chat.api.json;

import com.google.common.collect.ImmutableList;
import glitch.core.api.json.OrdinalList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChatRoomList implements OrdinalList<ChatRoom> {
    private final ImmutableList<ChatRoom> data;
    private final int size;

    @Override
    public int size() {
        return size;
    }
}
