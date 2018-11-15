package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okio.ByteString;

/**
 * Queueing Message emits while you sending data into server
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueuedMessageEvent<S extends AbstractWebSocketService<S, ? extends Event<S>>> extends Event<S> {
    private final ByteString message;

    public QueuedMessageEvent(S client, ByteString message) {
        super(client);
        this.message = message;
    }
}
