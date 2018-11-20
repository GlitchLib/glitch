package glitch.chat.events.interfaces;

import glitch.chat.object.entities.ChatRoomEntity;
import reactor.core.publisher.Mono;

public interface IChatRoom {
    Mono<ChatRoomEntity> getChatRoom();
}
