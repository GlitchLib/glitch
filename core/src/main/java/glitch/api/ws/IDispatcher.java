package glitch.api.ws;

import glitch.api.ws.events.IEvent;
import glitch.service.AbstractWebSocketService;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public interface IDispatcher<S extends AbstractWebSocketService<S>> {

    <T extends IEvent<S>> T processOn(Class<T> type);

    void dispatch(IEvent<S> event);
}
