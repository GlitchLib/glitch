package glitch.chat.api.json;

import glitch.core.api.json.OrdinalList;
import java.util.AbstractList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChatRoomList extends AbstractList<ChatRoom> implements OrdinalList<ChatRoom> {
    private final List<ChatRoom> data;
    private final int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public ChatRoom get(int index) {
        return data.get(index);
    }
}
