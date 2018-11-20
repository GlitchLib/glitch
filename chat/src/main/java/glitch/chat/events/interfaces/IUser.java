package glitch.chat.events.interfaces;

import glitch.chat.object.entities.ChannelUserEntity;
import glitch.chat.object.entities.UserEntity;
import reactor.core.publisher.Mono;

public interface IUser {
    Mono<UserEntity> getUser();
}
