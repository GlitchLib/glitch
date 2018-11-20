package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import lombok.Data;
import lombok.ToString;
import okio.ByteString;

/**
 * Queueing Message emits while you sending data into server
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
public class QueuedMessageEvent<S extends AbstractWebSocketService<S>> implements IEvent<S> {
    @ToString.Exclude
    private final S client;
    private final ByteString message;
}
