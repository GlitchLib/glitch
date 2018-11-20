package glitch.chat.events.interfaces;

import glitch.chat.object.entities.ChannelEntity;
import reactor.core.publisher.Mono;

public interface IChannel {
    Mono<ChannelEntity> getChannel();
}
