package glitch.chat.object.entities;

import glitch.api.objects.json.interfaces.IDObject;
import glitch.chat.GlitchChat;
import reactor.core.publisher.Mono;

public interface AbstractEntity<E extends IDObject<Long>> {
    GlitchChat getClient();

    Mono<E> getData();
}
